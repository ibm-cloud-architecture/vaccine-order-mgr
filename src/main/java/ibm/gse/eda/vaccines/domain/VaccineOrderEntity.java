package ibm.gse.eda.vaccines.domain;

import java.time.LocalDate;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;

@MongoEntity(collection="TheOrder")
public class VaccineOrderEntity extends PanacheMongoEntity {
    public String deliveryLocation;
    public Integer priority = 0;
    public Long quantity; 
    public LocalDate deliveryDate;
    public OrderStatus status;
    public LocalDate creationDate;

	public static VaccineOrderEntity findByDestination(String destination) {
		return find("deliveryLocation", destination).firstResult();
	}

}