package ibm.gse.eda.vaccines.domain.events;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ShipmentPlanEvent {

    public String planID;
    public String orderID;
    public String from;
    public String departureDate;
    public String to;
    public String arrivalDate;
    public double quantity;
    public int reefers;
    public double cost;
    public String type;

    public ShipmentPlanEvent(){}
}
