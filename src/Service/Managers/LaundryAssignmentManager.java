package Service.Managers;

import Model.Customer;
import Model.Load;
import Model.ServiceStage;
import Model.ServiceType;
import Service.DataManager.Repo.CustomerRepo;
import Service.DataManager.Repo.LaundryStatRepo;

import java.time.LocalDateTime;

public class LaundryAssignmentManager {
        private final LaundryStatRepo laundryRepo = new LaundryStatRepo();
    public void registerLaundry(int cstID, double loadWeight, ServiceType st) {

        CustomerRepo customerRepo = new CustomerRepo();

        // STEP 1: Validate customer
        Customer customer = Validator.requireExists(customerRepo.findById(cstID), "Customer");

        // STEP 2: Validate weight
        Validator.requirePositive(loadWeight, "Laundry Weight");

        // STEP 3: Validate service type
        Validator.requireExists(st, "Service Type");

        // STEP 4: Determine initial queue
        ServiceStage initialStage = switch (st) {

            case FULL_WASH, WASHING_ONLY ->
                    ServiceStage.QUEUED_WASHER;

            case DRYER_ONLY ->
                    ServiceStage.QUEUED_DRYER;
        };

        // STEP 5: Record registration time
        LocalDateTime receivedAt = LocalDateTime.now();

        // STEP 6: Create Load object
        Load load = new Load(0, customer.getCustomerId(), loadWeight, initialStage, receivedAt);

        // STEP 7: Insert into repository
        laundryRepo.addEntry(load);
    }

    public void updateLoadWeight(int loadId, double newWeight){

        Validator.requirePositiveId(loadId, "Load ID");
        Load load = Validator.requireExists(laundryRepo.findById(loadId), "Load ID");
        Validator.requirePositive(newWeight,newWeight + " is too low, please use the recommended weight" );

        Load updLoad = new Load(load.getLoadId(),load.getCstIdFk(),newWeight,load.getLoadStat(),load.getTimestamp());

        laundryRepo.updateEntry(updLoad);
    }

    public void updateLoadState(int loadId, ServiceStage updStage){
        Validator.requirePositiveId(loadId, "Load ID");
        Load load = Validator.requireExists(laundryRepo.findById(loadId), "Load ID");
        ServiceStage currentStage = load.getLoadStat();

        boolean validTransition = switch (currentStage) {

            case QUEUED_WASHER ->
                            updStage == ServiceStage.WASHING ||
                            updStage == ServiceStage.CANCELED;

            case QUEUED_DRYER ->
                            updStage == ServiceStage.DRYING ||
                            updStage == ServiceStage.CANCELED;

            case WASHING ->
                            updStage == ServiceStage.TRANSFER ||
                            updStage == ServiceStage.COMPLETED ||
                            updStage == ServiceStage.MACHINE_BROKE_INTERRUPTION;

            case TRANSFER ->
                            updStage == ServiceStage.QUEUED_DRYER ||
                            updStage == ServiceStage.CANCELED;

            case DRYING ->
                            updStage == ServiceStage.COMPLETED ||
                            updStage == ServiceStage.MACHINE_BROKE_INTERRUPTION;

            case MACHINE_BROKE_INTERRUPTION ->
                            updStage == ServiceStage.QUEUED_WASHER ||
                            updStage == ServiceStage.QUEUED_DRYER ||
                            updStage == ServiceStage.CANCELED;

            case COMPLETED, CANCELED -> false;
        };

        Load updLoad = new Load(load.getLoadId(),load.getCstIdFk(),load.getLoadWeight(),updStage,load.getTimestamp());
        laundryRepo.updateEntryState(updLoad);

    }
}
