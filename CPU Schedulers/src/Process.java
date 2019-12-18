import java.awt.Color;
import java.util.ArrayList;

public class Process implements Comparable<Process>
{
	private String Name; // or ID (depends on how you will use it)
	private int BurstTime;
	private int ArrivalTime;
	private int Priority;
	private Color ProcessColor; // will use it in GUI
	private int WaitingTime;
	private int TurnaroundTime;

	private int QuantumTime; // we will use it in AG Scheduling
	ArrayList<Integer> HistoryOfQuantum = new ArrayList<Integer>(); 
	// you may need it to store all updates of the quantum
										// Time. 
	
	private int processingTime;
	
	private int StartTime; 
	private int AGFactor; //****
	private int LastTimeAged;
	
	private int EndTime;
	
	public Process()
	{}
	
	public Process(Process P)
	{
		Name =P.getName();
		BurstTime = P.getBurstTime();
		ArrivalTime = P.getArrivalTime();
		Priority = P.getPriority();
		ProcessColor = P.getColor();
		
		AGFactor = BurstTime + ArrivalTime + Priority;
		StartTime = -1; 
		
		LastTimeAged = ArrivalTime;//***
		setProcessingTime(BurstTime);		
	}
	
	public Process(String Name , int BurstTime , int ArrivalTime , int Priority , String clr)
	{
		this.Name = Name;
		this.BurstTime = BurstTime;
		this.ArrivalTime = ArrivalTime;
		this.Priority = Priority;	
		
		AGFactor = BurstTime + ArrivalTime + Priority;
		StartTime = -1; 
		
		LastTimeAged = ArrivalTime;//***
		setProcessingTime(BurstTime);
		
		setColor(clr);
	}

	void Execute(){
		System.out.println( "Process " + Name );
		processingTime--;
	}

	public void setName(String name) {
		Name = name;
	}

	public void setBurstTime(int burstTime) {
		BurstTime = burstTime;
	}

	public void setArrivalTime(int arrivalTime) {
		ArrivalTime = arrivalTime;
		LastTimeAged = ArrivalTime;
	}

	public void setPriority(int priority) {
		Priority = priority;
	}

	public void setWaitingTime(int waitingTime) {
		
		WaitingTime = waitingTime;
	}

	public void setTurnaroundTime(int turnaroundTime) {
		TurnaroundTime = turnaroundTime;
	}

	public String getName() {
		return Name;
	}

	public int getBurstTime() {
		return BurstTime;
	}

	public int getArrivalTime() {
		return ArrivalTime;
	}

	public int getPriority() {
		return Priority;
	}

	public int getWaitingTime() {
		return WaitingTime;
	}

	public int getTurnaroundTime() {
		return TurnaroundTime;
	}
	public void printProcess() {
		System.out.println("\nName : " + Name + "\nArrival Time : " + ArrivalTime + "\nBurstTime : " + BurstTime
				+ "\nPriority : " + Priority + "\nWaiting Time : "+ WaitingTime 
				+ "\nTurn Arround Time : " +  TurnaroundTime);
	}

	public int getQuantumTime() {
		return QuantumTime;
	}

	public void setQuantumTime(int quantumTime) {
		HistoryOfQuantum.add(QuantumTime);
		QuantumTime = quantumTime;
	}

	public int getStartTime() {
		return StartTime;
	}

	public void setStartTime(int startTime) {
		StartTime = startTime;
	}

	public int getAGFactor() {
		return AGFactor;
	}

	public void setAGFactor(int aGFactor) {
		AGFactor = aGFactor;
	}

	public int getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
	}

	public Color getColor() {
		return ProcessColor;
	}

	public void setColor(String color) {
		
		java.lang.reflect.Field field = null;
		try {
			field = Class.forName("java.awt.Color").getField(color.toLowerCase());
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // toLowerCase because the color fields are RED or red, not Red
		try {
			ProcessColor = (Color)field.get(null);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setColor(Color color) {
		ProcessColor = color;
	}

	@Override
	public int compareTo(Process o) {
		// TODO Auto-generated method stub
		return this.getArrivalTime() - o.getArrivalTime();
	}

	public int getLastTimeAged() {
		return LastTimeAged;
	}

	public void setLastTimeAged(int lastTimeAged) {
		LastTimeAged = lastTimeAged;
	}

	public int getEndTime() {
		return EndTime;
	}

	public void setEndTime(int endTime) {
		EndTime = endTime;
	}
	
	
}
