import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

public class AGScheduling 
{
	int CurrentTime;
	int ProcessNumber;
	int MainQTime;
	ArrayList<Process> DieList = new ArrayList<Process>();
	ArrayList<Process> WaitingQueue = new ArrayList<Process>();
	ArrayList<Process> Processes = new ArrayList<Process>();
	
	SchedulingGUI gui ;
	HashMap<String, Integer> colorMap  = new HashMap<String, Integer>();
	
	int count = 0;
	
	public AGScheduling(ArrayList<Process> P , int Quantum)
	{
		Processes = P;
		gui = new SchedulingGUI(P);
		int MinTime = 10000;
		MainQTime = 0;
		
		for(int i = 0 ; i < Processes.size() ; i++)
		{
			Processes.get(i).setQuantumTime(Quantum);
			colorMap.put(Processes.get(i).getName(), i);
			if(Processes.get(i).getArrivalTime() < MinTime) {
				MinTime =  Processes.get(i).getArrivalTime();
				ProcessNumber  = i;
			}
			MainQTime +=  Processes.get(i).getQuantumTime();
			
			colorMap.put(Processes.get(i).getName(), i);
			Processes.get(i).setProcessingTime(Processes.get(i).getBurstTime());
			Processes.get(i).setStartTime(-1);
			Processes.get(i).setAGFactor(
					Processes.get(i).getPriority() + Processes.get(i).getArrivalTime() + Processes.get(i).getBurstTime());
		}
		
		//MainQTime = 1;
		MainQTime = AverageQTime();
		CurrentTime = MinTime;
		
		System.out.println( "MainQTime : "+ MainQTime);
	}
	
	public void startScheduling()
	{
		gui.AddColor( 1 , colorMap.get(Processes.get(ProcessNumber).getName()), new Color(255,255,255), CurrentTime);
		
		while(!EndOfScheduling())
		{
			
			if(Processes.get(ProcessNumber).getStartTime() == -1)
				Processes.get(ProcessNumber).setStartTime(CurrentTime);
		
			int nonPreemptiveQ = (int) Math.ceil((float)Processes.get(ProcessNumber).getQuantumTime()/2.0);
			
			int PreemptiveQ = Processes.get(ProcessNumber).getQuantumTime() - nonPreemptiveQ;
			int DiffQ = PreemptiveQ;
			boolean interrupt = false , Die = false;
			
			//System.out.println("nonPreemptive = " +nonPreemptiveQ + " at " + CurrentTime);
			for(int i = 0 ; i < nonPreemptiveQ ; i++)
			{
				if(Processes.get(ProcessNumber).getProcessingTime() <= 0)
				{
					Die = true;
					break;
				}
				Exec();
			}
			int ID = searchLessAGFactor();
			
		//	System.out.println("Preemptive = " + PreemptiveQ + " at " + CurrentTime);
			if(!Die)
				for(int i = 0 ; i < PreemptiveQ ; i++)
				{
					addWaiting();
					ID = searchLessAGFactor();
					//System.out.println(ID);
					if(Processes.get(ProcessNumber).getProcessingTime() <= 0) {
						Die = true;
					}
					if(ID != ProcessNumber)
					{
						interrupt = true;
					}
					if(Die || interrupt)
						break;
					Exec();
					DiffQ--;
				}
			
			int nextNumber = -1;
			if(Processes.get(ProcessNumber).getProcessingTime() == 0) {
				Die = true;
			}
			
			if(interrupt && !Die)
			{
				int newQ = DiffQ + Processes.get(ProcessNumber).getQuantumTime();
				Processes.get(ProcessNumber).setQuantumTime(newQ);
				WaitingQueue.add(Processes.get(ProcessNumber));
			}
			else
			{
				if(Die)
				{
					// set  waiting and turn arround time
					Processes.get(ProcessNumber).setEndTime(CurrentTime-1);
					
					Processes.get(ProcessNumber).setTurnaroundTime(CurrentTime -
							Processes.get(ProcessNumber).getArrivalTime());
					
					Processes.get(ProcessNumber).setWaitingTime(Processes.get(ProcessNumber).getTurnaroundTime() 
							- Processes.get(ProcessNumber).getBurstTime());
				
					Process Died = Processes.get(ProcessNumber);
					Processes.get(ProcessNumber).setQuantumTime(0);
					WaitingQueue.remove(Died);
					DieList.add(Died);
				}
				else
				{
					WaitingQueue.add(Processes.get(ProcessNumber));
					int newQ = MainQTime + Processes.get(ProcessNumber).getQuantumTime();
					Processes.get(ProcessNumber).setQuantumTime(newQ);
				}
								
			}
			
			if(EndOfScheduling())
				break;
		
			if(interrupt && !Die)
				nextNumber = ID;
			else
				nextNumber = TopOfWaiting();
			
			ProcessNumber = nextNumber;
			WaitingQueue.remove(Processes.get(ProcessNumber));
			
		}
		
		System.out.println("END");
	}

	private int TopOfWaiting() {
		// TODO Auto-generated method stub
		int index = ProcessNumber;
		int i = 0;
		while(WaitingQueue.size() <=0)
		{
			System.out.println("empty at " + CurrentTime + " , " + i);
			addWaiting();
		    gui.AddColor( CurrentTime , ProcessNumber, new Color(255,255,255)); 	
	        if(i != 0)
	        	CurrentTime++;
	        i++;
		}
		if(i>1) CurrentTime--;
		Process Top = WaitingQueue.get(0);
		index = Processes.indexOf(Top);
		
		return index;
	}

	private int searchLessAGFactor() {
		// TODO Auto-generated method stub
		int Min = Processes.get(ProcessNumber).getAGFactor();
		int index = ProcessNumber;
		for(int i = 0 ; i < WaitingQueue.size() ; i++)
		{
			if(WaitingQueue.get(i).getAGFactor() < Min)
			{
				Min = WaitingQueue.get(i).getAGFactor();
				
				index = Processes.indexOf(WaitingQueue.get(i));
			}
		}
			
		return index;
	}

	private void addWaiting() {
		// TODO Auto-generated method stub
		for(int i = 0 ; i < Processes.size() ; i++)
		{
			if(CurrentTime >= Processes.get(i).getArrivalTime()  
					&& !WaitingQueue.contains(Processes.get(i)) && !DieList.contains(Processes.get(i))
					&& i != ProcessNumber)
			{
				WaitingQueue.add(Processes.get(i));
			}
		}
	}

	private boolean EndOfScheduling() {
		// TODO Auto-generated method stub
		for(int i = 0 ; i < Processes.size() ; i++)
		{
			if(Processes.get(i).getQuantumTime() != 0) {
				return false;
			}
		}
		return true;
	}
	
	private void Exec()
	{
		System.out.print("at T = " + CurrentTime + " ");
		Processes.get(ProcessNumber).Execute();
		gui.AddColor( CurrentTime+1 , colorMap.get(Processes.get(ProcessNumber).getName()), Processes.get(ProcessNumber).getColor());
		CurrentTime++;
	}

	public void showQuantumTime()
	{
		for(int i = 0 ; i < DieList.size() ;i++)
		{
			System.out.println("History Of Quantum for process : "+ DieList.get(i).getName());
			for(int j = 1 ; j < DieList.get(i).HistoryOfQuantum.size() ; j++)
			{
				System.out.print(DieList.get(i).HistoryOfQuantum.get(j) + " ");
			}
			System.out.println();
		}
	}
	
	public float getAverageWaitingTime()
	{
		float ave = 0;
		for(int i = 0 ; i < DieList.size() ; i++)
		{
			ave+= DieList.get(i).getWaitingTime();
		}
		ave /=  DieList.size();
		
		return ave;
	}
	
	public float getAverageTurnaroundTime()
	{
		float ave = 0;
		for(int i = 0 ; i < DieList.size() ; i++)
		{
			ave+= DieList.get(i).getTurnaroundTime();
		}
		ave /=  DieList.size();
		
		return ave;
	}
	
	public int AverageQTime()
	{
		float sum = 0;
		for(int i = 0 ; i < Processes.size() ; i++)
		{
			sum+=Processes.get(i).getQuantumTime();
		}
		
		int ave = (int) Math.ceil(((float)sum/Processes.size())*0.1);
		
		return ave;
	}
}


