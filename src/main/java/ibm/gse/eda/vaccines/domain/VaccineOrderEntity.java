package ibm.gse.eda.vaccines.domain;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class VaccineOrderEntity extends PanacheEntity {
    public String askingOrganization;
    public String deliveryLocation;
    public String vaccineType;
    public Integer priority = 0;
    public Long quantity; 
    public LocalDate deliveryDate;
    // derived attributes
    public OrderStatus status;
    public String creationDate;
    @OneToOne(cascade = CascadeType.MERGE)
    public OrderEvent orderEvent;

    public VaccineOrderEntity(){}

    public VaccineOrderEntity(String askingOrganization,
            String deliveryLocation,
            long quantity) {
        this.askingOrganization = askingOrganization;
        this.deliveryLocation = deliveryLocation;
        this.quantity = quantity;
    }

    public OrderEvent getOrderEvent() {
        return orderEvent;
    }

    public void setOrderEvent(OrderEvent orderEvent) {
        this.orderEvent = orderEvent;
    }
    
}