import java.awt.Color;
import java.util.ArrayList;

public class ShortestJobFirst {
   private ArrayList<Process> processes;
   private ArrayList<Integer> executedProcesses = new ArrayList<Integer>();

   SchedulingGUI gui ;
   
    public ShortestJobFirst(ArrayList<Process> processes) {
        this.processes = processes;
        gui = new SchedulingGUI(processes);
    }
    
    public void startScheduling(){
        int currTime=1;
        int currProcess=0;
        for (int i = 0; i <processes.size() ; i++) {
        	int temp = currProcess;
            do {
                currProcess = getNextProcessNumber(currTime);
                if (currProcess==-1) {
                	currTime++;
                	gui.AddColor( currTime , temp, new Color(255,255,255));	
                }
            }while (currProcess==-1);
            
            processes.get(currProcess).Execute();
        	gui.AddColor( currTime+1 , currProcess, processes.get(currProcess).getColor() ,processes.get(currProcess).getBurstTime() );
    		
            processes.get(currProcess).setWaitingTime(currTime-processes.get(currProcess).getArrivalTime());
            processes.get(currProcess).setTurnaroundTime(processes.get(currProcess).getWaitingTime()+processes.get(currProcess).getBurstTime());
            currTime+=processes.get(currProcess).getBurstTime();
            executedProcesses.add(currProcess);

        }
    }
    private int getNextProcessNumber(int currTime){
        int nextProcessNumber=-1;
        for(int i=0;i<processes.size();i++){
            if(!executedProcesses.contains(i) && processes.get(i).getArrivalTime()<=currTime){ // the next process must be arrived and must have
                                                                                               // the lest BurstTime
               if(nextProcessNumber==-1) { nextProcessNumber=i; } // if it's the first process
               else if(processes.get(i).getBurstTime()<processes.get(nextProcessNumber).getBurstTime()){
                   nextProcessNumber=i;
               }
            }
        }
        return nextProcessNumber;
    }
    
	public double getAverageWaitingTime() {
		double sumOfWaiting = 0.0;
		for(Process p : processes) {
			sumOfWaiting+=p.getWaitingTime();
		}
		return sumOfWaiting / processes.size();
	}
	public double getAverageTurnaroundTime() {
		double sumOfTurnAround = 0.0;
		for(Process p : processes) {
			sumOfTurnAround+=p.getTurnaroundTime();
		}

		return sumOfTurnAround / processes.size();
	}
}
