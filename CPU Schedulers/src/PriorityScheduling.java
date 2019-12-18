import java.awt.Color;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class PriorityScheduling {
	ArrayList<Process> Processes = new ArrayList<Process>(); //which processes havent yet been processed
	ArrayList<Process> WaitingQueue = new ArrayList<Process>(); //which of above queue has arrived and is waiting
	ArrayList<Process> executedProcesses = new ArrayList<Process>(); //which have already been processed
	ArrayList<Process> copy ;//which have already been processed

	SchedulingGUI gui ;
	
	int CurrentTime = 0 ;
	int agingValue = 5 ;
	
	PriorityScheduling(ArrayList<Process> Q){
		for(Process i : Q)
		{
			Processes.add(new Process(i));
		}	
        
		
		gui = new SchedulingGUI(Processes);
		copy = new ArrayList<Process>(Processes);
		
		Processes.sort(null);
		CurrentTime = Processes.get(0).getArrivalTime();
		ConstructWaitingQueue(CurrentTime);

	}
	
	public void setAgingValue(int val) {
		agingValue = val;
	}
	
	public void startScheduling() {
		Process currentProcess = new Process();
		
		gui.AddColor( 1 , copy.indexOf(currentProcess), new Color(255,255,255), CurrentTime-1);
		
		while(Processes.size() > 0 ) {
			if(FindMaxPriorityInWaiting()==null) {
				CurrentTime++;
				gui.AddColor( CurrentTime ,copy.indexOf(currentProcess) , new Color(255,255,255)); 	
				
				ConstructWaitingQueue(CurrentTime);	
			}
			else {
			currentProcess = FindMaxPriorityInWaiting();
			gui.AddColor(CurrentTime+1 , copy.indexOf(currentProcess) , currentProcess.getColor(), currentProcess.getBurstTime());

			currentProcess.setStartTime(CurrentTime);
			CurrentTime += currentProcess.getBurstTime();

			currentProcess.setWaitingTime( currentProcess.getStartTime() - currentProcess.getArrivalTime());
			currentProcess.setTurnaroundTime(currentProcess.getWaitingTime() + currentProcess.getBurstTime() );
			
			executedProcesses.add(currentProcess);
			Processes.remove(currentProcess);
			
			ConstructWaitingQueue(CurrentTime);
			AgingProcess(agingValue);
			currentProcess.Execute();

			}
		}
	}
	
	public double getAverageWaiting() {
		double sumOfWaiting = 0.0;
		for(Process p : executedProcesses) {
			sumOfWaiting+=p.getWaitingTime();
		}
		return sumOfWaiting / executedProcesses.size();
	}
	public double getAverageTurnAround() {
		double sumOfTurnAround = 0.0;
		for(Process p : executedProcesses) {
			sumOfTurnAround+=p.getTurnaroundTime();
		}
		return sumOfTurnAround / executedProcesses.size();
	}
	private Process FindMaxPriorityInWaiting() {
		Process maxPriority = null;
		if(WaitingQueue.size()>0) {
			maxPriority = WaitingQueue.get(0);
			for(int i = 1 ; i< WaitingQueue.size() ; i++) {
				if(maxPriority.getPriority() >= WaitingQueue.get(i).getPriority()) {
					if(maxPriority.getPriority() == WaitingQueue.get(i).getPriority())
						maxPriority =(maxPriority.getArrivalTime() > WaitingQueue.get(i).getArrivalTime() ?WaitingQueue.get(i) :maxPriority);
					else
						maxPriority = WaitingQueue.get(i);
				}
			}
		}
		return maxPriority ;
	}
	
	private void ConstructWaitingQueue(int currentTime) {
		WaitingQueue = new ArrayList<Process>();
		for(int i = 0 ; i < Processes.size() ; i++ )
			if(Processes.get(i).getArrivalTime() <= currentTime) {
				WaitingQueue.add(Processes.get(i));
			}
			else break;
	}	
	
	private void AgingProcess(int timeNeededToChange) {
		Process p = new Process();
		for(int i = 0 ; i < WaitingQueue.size() ; i++ ) {
				p = WaitingQueue.get(i);
				if(p.getPriority() > 0 && p!= FindMaxPriorityInWaiting()) {
					int nIncreasesInPrioroty = (CurrentTime - p.getLastTimeAged()) / timeNeededToChange;
					
					nIncreasesInPrioroty = (nIncreasesInPrioroty> 0? nIncreasesInPrioroty: 0);
					
					p.setPriority(p.getPriority() - nIncreasesInPrioroty);
					p.setLastTimeAged(CurrentTime + ((CurrentTime - p.getLastTimeAged()) % timeNeededToChange));
			}
		}
	}
	
}

