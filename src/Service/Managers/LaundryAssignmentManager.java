package Service.Managers;

import Model.Customer;
import Model.Load;
import Model.ServiceStage;
import Model.ServiceType;
import Service.DataManager.Repo.AssRepo;
import Service.DataManager.Repo.CustomerRepo;
import Service.DataManager.Repo.LaundryStatRepo;

import java.time.LocalDateTime;

public class LaundryAssignmentManager {
    public void registerLaundry(int cstID, double loadWeight, ServiceType st) {
        LaundryStatRepo laundryRepo = new LaundryStatRepo();
        CustomerRepo customerRepo = new CustomerRepo();
        int customerId = 0;

        // STEP 1: Validate customer
        //Validator.requireExists(cstID, "Customer");

        Customer customer = Validator.requireExists(
                customerRepo.findById(cstID),
                "Customer"
        );

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
        Load load = new Load(
                0,
                customer.getCustomerId(),
                loadWeight,
                initialStage,
                receivedAt
        );

        // STEP 7: Insert into repository
        laundryRepo.addEntry(load);
    }
}
