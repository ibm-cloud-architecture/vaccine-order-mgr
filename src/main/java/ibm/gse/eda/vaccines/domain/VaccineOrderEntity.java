package ibm.gse.eda.vaccines.domain;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class VaccineOrderEntity extends PanacheEntity{
    public String askingOrganization;
    public String deliveryLocation;
    public String vaccineType;
    public Integer priority = 0;
    public Long quantity; 
    public String deliveryDate;
    // derived attributes
    public OrderStatus status;
    public String creationDate;

    public VaccineOrderEntity(){}

    public VaccineOrderEntity(String askingOrganization,
            String deliveryLocation,
            long quantity) {
        this.askingOrganization = askingOrganization;
        this.deliveryLocation = deliveryLocation;
        this.quantity = quantity;
    }

   
    
}