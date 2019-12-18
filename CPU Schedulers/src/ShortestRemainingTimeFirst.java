
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class ShortestRemainingTimeFirst extends Process {
    private ArrayList<Process> Processes = new ArrayList<>();
    private ArrayList<Process> TempProcesses = new ArrayList<>();

    private ArrayList<Process> ExecutedProcesses = new ArrayList<>();
    private HashMap<String, Integer> ProcessesBurstTime = new HashMap<>();
    private int ContextSwitching;

    SchedulingGUI gui ;

    public ShortestRemainingTimeFirst(ArrayList<Process> processes, int ContextSwitching) {
        this.TempProcesses = processes;
        this.ContextSwitching = ContextSwitching;
        for(int i = 0 ; i < processes.size() ;i++) {
            ProcessesBurstTime.put(TempProcesses.get(i).getName(),TempProcesses.get(i).getBurstTime());
        }
        for(Process o: processes) {
            Processes.add(new Process(o)); //*****************************
        }

        gui = new SchedulingGUI(processes);
    }

    public ArrayList<Process> startScheduling(){
        int CurrentTime = 0;
        Process CurrProcess, TempProcess;
        int BurstTime;
        int StartTime =0;
        int i =0;
        int count =0;
        double TotalWT = 0.0;
        double TotalTAT = 0.0;
        double AverWT = 0.0;
        double AverTAT = 0.0;
        //Looping in process array to execute all processes

        while(!Processes.isEmpty()){
            CurrProcess = GetArrivedProcess(Processes.get(i), CurrentTime);
            if(CurrProcess.getArrivalTime() > CurrentTime){
                do{
                    CurrentTime++;
                    gui.AddColor( CurrentTime , GetTempProcess(CurrProcess.getName()) , new Color(255,255,255)); //***************
                }while(CurrProcess.getArrivalTime() != CurrentTime);
            }
            do{
                TempProcess = LeastBurstTime(CurrProcess, CurrentTime);
                if(CurrProcess != TempProcess && count >0) {
                    AddContextBlocks(CurrProcess ,CurrentTime , ContextSwitching);
                    CurrentTime += ContextSwitching;
                }
                CurrProcess = TempProcess;

                gui.AddColor( CurrentTime+1 ,  GetTempProcess(CurrProcess.getName()) , CurrProcess.getColor());
                TempProcess.Execute();
                BurstTime = CurrProcess.getBurstTime();
                BurstTime--;
                CurrentTime++;
                CurrProcess.setBurstTime(BurstTime);
                count++;
            }while(BurstTime != 0);

            AddContextBlocks(CurrProcess ,CurrentTime , ContextSwitching);

            CurrentTime += ContextSwitching;


            CurrProcess.setTurnaroundTime(CurrentTime - CurrProcess.getArrivalTime());
            CurrProcess.setWaitingTime(CurrProcess.getTurnaroundTime() - ProcessesBurstTime.get(CurrProcess.getName()));

//****************************
            TempProcesses.get(GetTempProcess(CurrProcess.getName())).setWaitingTime(CurrProcess.getWaitingTime());
            TempProcesses.get(GetTempProcess(CurrProcess.getName())).setTurnaroundTime(CurrProcess.getTurnaroundTime());
//***************************

            System.out.println("Process Waiting Time : " + CurrProcess.getWaitingTime());
            System.out.println("Process TurnAround Time : " + CurrProcess.getTurnaroundTime());
            Processes.remove(CurrProcess);
            ExecutedProcesses.add(CurrProcess);
            count =0;
        }

        for (Process o : ExecutedProcesses){
            TotalTAT += o.getTurnaroundTime();
            TotalWT += o.getWaitingTime();
        }
        AverTAT = TotalTAT/ExecutedProcesses.size();
        AverWT = TotalWT/ExecutedProcesses.size();
        System.out.println("Average TurnAround Time is : " + AverTAT);
        System.out.println("Average Waiting Time is : " + AverWT);

        return TempProcesses;
    }

    private void AddContextBlocks(Process currProcess, int currentTime, int contextSwitching2) {
        // TODO Auto-generated method stub
        Process p = new Process();
        p.setColor("black");
        gui.AddColor( currentTime+1 , GetTempProcess(currProcess.getName()) , p.getColor(),contextSwitching2);
    }

    Process LeastBurstTime(Process Curr, int CurrentTime){
        for(int j=0; j<Processes.size(); j++){
            if(Processes.get(j).getArrivalTime() <= CurrentTime && Processes.get(j).getBurstTime() < Curr.getBurstTime())
                Curr = Processes.get(j);
        }
        return Curr;
    }
    Process GetArrivedProcess(Process Curr, int CurrentTime){
        for(int j=0; j<Processes.size(); j++){
            if(Processes.get(j).getArrivalTime() < Curr.getArrivalTime())
                Curr = Processes.get(j);
        }
        return Curr;
    }

    int GetTempProcess(String ProcessName) {
        Process temp = null;
        int index=0;
        for (int j = 0; j < TempProcesses.size(); j++) {
            temp = TempProcesses.get(j);
            if((temp.getName()).equals(ProcessName))
                return j;
        }
        return index;
    }
}