package ibm.gse.eda.vaccines.domain;

import java.time.LocalDate;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class OrderEvent extends PanacheEntity {
    public static final String ORDER_CREATED = "OrderCreated";
    public static final String ORDER_SCHEDULED = "OrderScheduled";
    public static final String ORDER_PENDING = "OrderPending";
    public static final String ORDER_CLOSED = "OrderClosed";
    public static final String ORDER_CANCELLED = "OrderCancelled";
    public Long orderID;
    public String askingOrganization;
    public String deliveryLocation;
    public String vaccineType;
    public Integer priority = 0;
    public Long quantity; 
    public LocalDate deliveryDate;
    public String type;
    public OrderEvent(){}
    
    public static OrderEvent fromEntity(VaccineOrderEntity order) {
        OrderEvent event = new OrderEvent();
        event.orderID = order.id;
        event.deliveryLocation = order.deliveryLocation;
        event.quantity = order.quantity;
        event.priority = order.priority;
        event.deliveryDate = order.deliveryDate;
       return event;
	}
}
