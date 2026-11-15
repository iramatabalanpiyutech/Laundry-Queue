package Service.Managers;
import Model.Machine;
import Model.MachineState;
import Model.MachineType;
import Service.DataManager.Repo.MachineRepo;
import java.util.List;

public class MachineManager{
    private final MachineRepo mr = new MachineRepo();
    private final int TEMP_ID = 0;


    public void registerMachine(MachineType mt, int weight, MachineState ms){
        Validator.requireExists(mt, "Machine Type");
        Validator.requirePositive(weight, "Capacity");
        Validator.requireExists(ms,"Operational Status");

        Machine machine = new Machine (TEMP_ID,weight,mt,ms);
        mr.addEntry(machine);
    }

    public void updateMachineState(int id, MachineState newState){

        Validator.requirePositiveId(id, "Machine ID");
        Machine machine = Validator.requireExists(mr.findById(id), "Machine Id");
        Validator.requireExists(newState, "New Machine State");
       MachineState currentState = machine.getMachineState();
       MachineType retainMachState = machine.getMachineType();
       double retainWeight = machine.getMachineCapacity();




       boolean validTransition = switch (currentState){
           case AVAILABLE -> newState == MachineState.IN_USE || newState == MachineState.MAINTENANCE || newState == MachineState.RETIRED;
           case IN_USE -> newState == MachineState.AVAILABLE || newState == MachineState.OUT_OF_SERVICE;
           case MAINTENANCE -> newState == MachineState.AVAILABLE || newState == MachineState.OUT_OF_SERVICE;
           case OUT_OF_SERVICE -> newState == MachineState.MAINTENANCE || newState == MachineState.RETIRED;
           case TRANSFER -> newState == MachineState.AVAILABLE;
           case REASSIGN -> newState == MachineState.OUT_OF_SERVICE || newState == MachineState.MAINTENANCE;
           case RETIRED -> false;
       };

        Validator.requireState(validTransition, "Status Transition failed from " + currentState + " to " + newState);
       Machine updMachStat = new Machine(id, retainWeight, retainMachState, newState);
       mr.updateEntry(updMachStat);

    }

    public void retireMachine(int id){
        Machine machine = Validator.requireExists(mr.findById(id), "Machine ID");
        Validator.requirePositiveId(id, "Machine ID");

        MachineState currentState = machine.getMachineState();
        MachineType retainMachState = machine.getMachineType();
        double retainWeight = machine.getMachineCapacity();

        boolean allowedToDelete = currentState == MachineState.RETIRED;

        Validator.requireState(allowedToDelete, "Cannot delete machine with state set on " + currentState);

        updateMachineState(id, MachineState.RETIRED);
    }

    public List<Machine> getAllMachine(){
        return mr.findAll();
    }

}
