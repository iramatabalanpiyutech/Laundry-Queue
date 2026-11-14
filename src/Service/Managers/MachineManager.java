package Service.Managers;
import Model.Machine;
import Model.MachineState;
import Model.MachineType;
import Service.Managers.Validator;
import Service.DataManager.Repo.MachineRepo;

import javax.crypto.Mac;
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

        Machine machine = Validator.requireExists(mr.findById(id), "Machine Id");

       MachineState currentState = machine.getMachineState();
       MachineType retainMachState = machine.getMachineType();
       double retainWeight = machine.getMachineCapacity();

       Validator.requirePositiveId(id, "Machine ID");

       boolean validTransition = switch (currentState){
           case AVAILABLE -> newState == MachineState.IN_USE || newState == MachineState.MAINTENANCE;
           case IN_USE -> newState == MachineState.AVAILABLE || newState == MachineState.OUT_OF_SERVICE;
           case MAINTENANCE -> newState == MachineState.AVAILABLE || newState == MachineState.OUT_OF_SERVICE;
           case OUT_OF_SERVICE -> newState == MachineState.MAINTENANCE;
           case TRANSFER -> newState == MachineState.AVAILABLE;
           case REASSIGN -> newState == MachineState.OUT_OF_SERVICE || newState == MachineState.MAINTENANCE;
       };

       Machine updMachStat = new Machine(id, retainWeight, retainMachState, newState);
       mr.updateEntry(updMachStat);

    }

    public void removeMachine(int id){
        Machine machine = Validator.requireExists(mr.findById(id), "Machine ID");
        Validator.requirePositiveId(id, "Machine ID");

        MachineState currentState = machine.getMachineState();
        MachineType retainMachState = machine.getMachineType();
        double retainWeight = machine.getMachineCapacity();

        boolean allowedToDelete = currentState == MachineState.AVAILABLE || currentState == MachineState.OUT_OF_SERVICE;

        Validator.requireState(allowedToDelete, "Cannot delete machine with state set on " + currentState);

        mr.deleteEntry(id);
    }

    public List<Machine> getAllMachine(){
        return mr.findAll();
    }

}
