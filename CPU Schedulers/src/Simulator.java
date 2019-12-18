import java.util.ArrayList;
import java.util.Scanner;

public class Simulator {
	
	static public void main (String[] arg)
	{
		int nProcesses;
		int RRQuantum;
		int contextSwitching;
		
		ArrayList<Process> Processes = new ArrayList<Process>();

		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		
		System.out.println("Enter n of processes :");
		nProcesses = input.nextInt();
		
		System.out.println("Enter Round Robin Time Quantum :");
		RRQuantum = input.nextInt();
		
		System.out.println("Enter context Switching Time :");
		contextSwitching = input.nextInt();
		
		// Read Processes
		for(int i = 0 ; i < nProcesses ;i++)
		{
			input = new Scanner(System.in);
			Process p = new Process();
			System.out.println( (i+1) + " Enter Process Name :");
			p.setName(input.nextLine());
		
			System.out.println("Enter Process Burst Time :");
			p.setBurstTime(input.nextInt())  ;
		
			System.out.println("Enter Process Arraival Time :");
			p.setArrivalTime(input.nextInt()) ;
			
			System.out.println("Enter Process prority :");
			p.setPriority(input.nextInt());
			
			input = new Scanner(System.in);
			System.out.println( "Enter Process Color :");
			p.setColor(input.nextLine());
			
			Processes.add(p);	
		}
		
		System.out.println("\nSelect the Scheduler you want to use : "
				+ "\n1-Non-Preemptive Shortest- Job First (SJF)"
				+ "\n2-Shortest-Remaining Time First (SRTF) Scheduling"
				+ "\n3-Non-preemptive Priority Scheduling."
				+ "\n4-AG Scheduling \n5-End");
		int select = input.nextInt();
		if(select == 1)
		{
			ShortestJobFirst shortestJobFirst= new ShortestJobFirst (Processes);
			shortestJobFirst.startScheduling();
			System.out.println( "\nAverage Waiting Time :  " + shortestJobFirst.getAverageWaitingTime());
			System.out.println("Average Turnaround Time :" + shortestJobFirst.getAverageTurnaroundTime() + "\n");
		}
		else if(select == 2)
		{
			ShortestRemainingTimeFirst first = new ShortestRemainingTimeFirst(Processes, contextSwitching);
			Processes = first.startScheduling();
		}
		else if(select == 3)
		{
			PriorityScheduling pScheduling = new PriorityScheduling(Processes);
			pScheduling.startScheduling();
	
			System.out.println( "\nAverage Waiting Time :  " + pScheduling.getAverageWaiting());
			System.out.println("Average Turnaround Time :" + pScheduling.getAverageTurnAround() + "\n");
		}
		else if(select == 4) 
		{
			AGScheduling agSch = new AGScheduling(Processes, RRQuantum);
			agSch.startScheduling();
	
			agSch.showQuantumTime();
			System.out.println( "Average Waiting Time :  " + agSch.getAverageWaitingTime());
			System.out.println("Average Turnaround Time :" + agSch.getAverageTurnaroundTime());
		}
		else
		{
			System.out.println("Invalid selection");
		}
	
		for(int i = 0 ; i < Processes.size();i++)
			Processes.get(i).printProcess();
	}

}
