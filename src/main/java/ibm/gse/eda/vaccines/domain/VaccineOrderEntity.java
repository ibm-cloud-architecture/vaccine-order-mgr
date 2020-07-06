package ibm.gse.eda.vaccines.domain;

import java.time.LocalDate;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class VaccineOrderEntity extends PanacheEntity {
    public String deliveryLocation;
    public Integer priority = 0;
    public Long quantity; 
    public LocalDate deliveryDate;
    public OrderStatus status;
    public String creationDate;

	public static VaccineOrderEntity findByDestination(String destination) {
		return find("deliveryLocation", destination).firstResult();
	}

}