package Model;

public enum ServiceStage {
    QUEUED_WASHER("Queued_Washer"),
    QUEUED_DRYER("Queued_Dryer"),
    WASHING("Washing"),
    TRANSFER("Transfer"),
    DRYING("Drying"),
    COMPLETED("Completed"),

    CANCELED("Canceled"),
    INTERRUPTION("interruption");


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
