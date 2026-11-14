package Model;

public enum ServiceStage {
    QUEUED_WASHER("Queued_Washer"),
    QUEUED_DRYER("Queued_Dryer"),
    WASHING("Washing"),
    TRANSFER("Transferring"),
    DRYING("Drying"),
    COMPLETED("Finished"),
    CANCELED("Canceled"),
    MACHINE_BROKE_INTERRUPTION("Machine_broke_interruption");


    private final String servState;
    ServiceStage(String servState) {
        this.servState = servState;
    }

    public String getServState() {
        return servState;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
