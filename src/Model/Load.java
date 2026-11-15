
package Model;

import java.time.LocalDateTime;

public class Load {
    private int loadId;
    private int cstIdFk;
    private ServiceType serviceType;
    private double loadWeight;
    private ServiceStage loadStat;

    private LocalDateTime timestamp;

    public Load(int loadId, int cstIdFk, double loadWeight, ServiceStage loadStat, LocalDateTime timestamp, ServiceType ServiceType) {
        this.loadId = loadId;
        this.cstIdFk = cstIdFk;
        this.loadWeight = loadWeight;
        this.loadStat = loadStat;
        this.timestamp = timestamp;
        this.serviceType = ServiceType;
    }

    public int getLoadId() {
        return loadId;
    }

    public int getCstIdFk() {
        return cstIdFk;
    }

    public double getLoadWeight() {
        return loadWeight;
    }

    public ServiceStage getLoadStat() {
        return loadStat;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setCstIdFk(int cstIdFk) {
        this.cstIdFk = cstIdFk;
    }

    public void setLoadId(int loadId) {
        this.loadId = loadId;
    }
}
