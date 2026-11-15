package Service.Managers;

import Model.*;
import Service.DataManager.Repo.CustomerRepo;
import Service.DataManager.Repo.LaundryStatRepo;
import Service.DataManager.Repo.MachineAsignmentRepo;
import Service.DataManager.Repo.MachineRepo;
import Service.Exception.UnavailableMachineException;

public class MachineAssignmentManager {
    private LaundryStatRepo laundryStatRepo;
    private MachineRepo machineRepo;
    private MachineAsignmentRepo mar;
    private final int TEMP_ID = 0;

    public void assignMachine(int loadId, int machineId, int durationMinutes) {
        // STEP 1: Validate input
        Validator.requirePositiveId(loadId, "Load ID is negative");
        Validator.requirePositiveId(machineId, "Machine ID is negative");
        Validator.requirePositive(durationMinutes, "Duration is too low");

        // STEP 2: Retrieve load and machine
        Load load = Validator.requireExists(laundryStatRepo.findById(loadId), "You haven't registered a laundry with the ID: " + loadId);
        Machine machine = Validator.requireExists(machineRepo.findById(machineId), "The Machine you somehow Assigned doesn't exist");

        // STEP 3: Check machine availability
        boolean isAvailable =
                machine.getMachineState() == MachineState.AVAILABLE;
        
        if(!isAvailable){
            throw new UnavailableMachineException("Machine Chosen is Not Available");
        }

        // STEP 4: Check machine capacity
        if(machine.getMachineCapacity() < load.getLoadWeight()){
            throw new IllegalArgumentException("Weight is over the recommended washer/dryer capacity");
        }
        // STEP 5: Check machine type matches queue
        boolean chkStat = switch (load.getLoadStat()) {
            case QUEUED_WASHER ->
                    machine.getMachineType() == MachineType.WASHER;
            case QUEUED_DRYER ->
                    machine.getMachineType() == MachineType.DRYER;

            default -> false;
        };

        if(!chkStat){
            throw new IllegalArgumentException("Machine type does not match load queue status.");
        }
        // STEP 6: Check neither is already assigned
        boolean loadAssigned = mar.hasActiveLoadAssignment(load.getLoadId());
        boolean machineAssigned = mar.hasActiveMachineAssignment(machine.getMachineId());
        Validator.requireState(!loadAssigned, "Load is already assigned!");
        Validator.requireState(!machineAssigned,"Machine is already occupied!");
        // STEP 7: Create assignment and update states
        MachineAssignment machineAssignment = new MachineAssignment(TEMP_ID,load.getLoadId(),machine.getMachineId(),null,durationMinutes,null);
        mar.addEntry(machineAssignment);
    }

    // TODO 2: Start washing/drying session
    public void startSession(int assignmentId) {
        // TODO: Retrieve machine assignment
        // TODO: Retrieve associated machine and load
        // TODO: Validate assignment exists
        // TODO: Verify session hasn't started or ended
        // TODO: Determine operation duration
        // TODO: Set STARTED_AT to current timestamp
        // TODO: Set machine state to IN_USE
        // TODO: Set load state to WASHING or DRYING
        // TODO: Save changes in one transaction
    }

    // TODO 3: Get remaining session time
    public long getRemainingSeconds(int assignmentId) {
        // TODO: Retrieve assignment
        // TODO: Retrieve operation duration
        // TODO: Calculate expected completion time
        // TODO: Calculate remaining seconds
        // TODO: Return 0 if time has expired
        return 0;
    }

    // TODO 4: Complete washing/drying session
    public void completeSession(int assignmentId) {
        // TODO: Retrieve assignment, machine, and load
        // TODO: Validate session is active
        // TODO: Verify completion conditions
        // TODO: Set ENDED_AT to current timestamp
        // TODO: Update machine availability
        // TODO: If washer, move load to dryer queue/transfer state
        // TODO: If dryer, mark load COMPLETED
        // TODO: Save all changes in one transaction
    }

    // TODO 5: Transfer washed laundry to dryer
    public void transferLoad(int loadId, int dryerId) {
        // TODO: Retrieve load and dryer
        // TODO: Verify washing has finished
        // TODO: Check dryer availability
        // TODO: Verify dryer type
        // TODO: Ensure no conflicting active assignments
        // TODO: Apply your washer-to-top-dryer business rule
        // TODO: Create dryer assignment
        // TODO: Update load and machine states
        // TODO: Save changes atomically
    }

    // TODO 6: Handle machine interruption
    public void interruptSession(int assignmentId) {
        // TODO: Retrieve active assignment
        // TODO: Verify session is running
        // TODO: Record interruption time
        // TODO: End or suspend assignment according to business rules
        // TODO: Update machine state
        // TODO: Update load state to MACHINE_BROKE_INTERRUPTION
        // TODO: Save changes
    }

    // TODO 7: Reassign interrupted laundry
    public void reassignLoad(int loadId, int newMachineId) {
        // TODO: Retrieve interrupted load
        // TODO: Retrieve replacement machine
        // TODO: Validate machine compatibility
        // TODO: Check availability
        // TODO: Ensure previous assignment is closed
        // TODO: Decide how remaining duration is preserved
        // TODO: Create replacement assignment
        // TODO: Update relevant states
        // TODO: Save changes atomically
    }

    // TODO 8: Cancel laundry assignment
    public void cancelAssignment(int assignmentId) {
        // TODO: Retrieve assignment
        // TODO: Check whether cancellation is allowed
        // TODO: End active assignment if applicable
        // TODO: Update load state to CANCELED
        // TODO: Release machine when appropriate
        // TODO: Save changes atomically
    }
}
