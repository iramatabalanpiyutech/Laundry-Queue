package Service.Managers;
import java.util.*;
import Model.Load;

public class QueueManager {

    // TODO: Retrieve loads waiting for washers
    public List<Load> getWasherQueue() {
        // TODO: Find loads with QUEUED_WASHER
        // TODO: Order by received time (FIFO)
        return null;
    }

    // TODO: Retrieve loads waiting for dryers
    public List<Load> getDryerQueue() {
        // TODO: Find loads with QUEUED_DRYER
        // TODO: Order by queue entry time
        return null;
    }

    // TODO: Find the next eligible washing load
    public Load getNextWasherLoad() {
        // TODO: Retrieve washer queue
        // TODO: Return first load, or null if empty
        return null;
    }

    // TODO: Find the next eligible drying load
    public Load getNextDryerLoad() {
        // TODO: Retrieve dryer queue
        // TODO: Apply dryer priority rules
        // TODO: Return next eligible load
        return null;
    }
}