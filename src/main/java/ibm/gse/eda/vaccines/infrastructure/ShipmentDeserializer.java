package ibm.gse.eda.vaccines.infrastructure;

import ibm.gse.eda.vaccines.domain.events.ShipmentPlanEvent;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class ShipmentDeserializer extends JsonbDeserializer<ShipmentPlanEvent> {
    public ShipmentDeserializer(){
        // pass the class to the parent.
        super(ShipmentPlanEvent.class);
    }
    
}
