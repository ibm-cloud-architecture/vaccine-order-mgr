package ibm.gse.eda.vaccines.api.dto;

import ibm.gse.eda.vaccines.domain.events.ShipmentPlanEvent;


public class ShipmentPlan {
    public String orderID;
    public String from;
    public String departureDate;
    public String to;
    public String arrivalDate;
    public double quantity;
    public int reefers;
    public double cost;
    public String type;


	public static ShipmentPlan from(ShipmentPlanEvent evt) {
        ShipmentPlan sp = new ShipmentPlan();
        sp.type = evt.type;
        sp.from = evt.from;
        sp.departureDate = evt.departureDate;
        sp.to = evt.to;
        sp.arrivalDate = evt.arrivalDate;
        sp.cost = evt.cost;
        sp.quantity = evt.quantity;
        sp.reefers = evt.reefers;
		return sp;
	}
}
