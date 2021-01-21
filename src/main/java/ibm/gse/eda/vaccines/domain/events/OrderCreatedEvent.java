package ibm.gse.eda.vaccines.domain.events;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ibm.gse.eda.vaccines.domain.VaccineOrderEntity;
import io.debezium.outbox.quarkus.ExportedEvent;

@Entity
public class OrderCreatedEvent implements ExportedEvent<String, String> {

    private static ObjectMapper mapper = new ObjectMapper();
    @Id
    public long id;
    @Column(length=2046)
    public String payload;
    public Instant timestamp;
    
    public OrderCreatedEvent(){}

    public OrderCreatedEvent(long id, JsonNode order) {
        this.id = id;
        try {
            this.payload = mapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            this.payload = "";
        }
        this.timestamp = Instant.now();
    }

    public static OrderCreatedEvent of(VaccineOrderEntity order) {
        ObjectNode asJson = mapper.createObjectNode()
        .put("orderID", order.id)
        .put("deliveryLocation", order.deliveryLocation)
        .put("quantity", order.quantity)
        .put("priority", order.priority)
        .put("deliveryDate", order.deliveryDate.toString())
        .put("askingOrganization",order.askingOrganization)
        .put("vaccineType",order.vaccineType)
        .put("status",order.status.name())
        .put("creationDate",order.creationDate.toString());
        return new OrderCreatedEvent(order.id, asJson);
    }

    @Override
    public String getAggregateId() {
        return String.valueOf(id);
    }

    @Override
    public String getAggregateType() {
        return "VaccineOrderEntity";
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public Instant getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String getType() {
        return "OrderCreated";
    }
}
