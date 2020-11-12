package ibm.gse.eda.vaccines.domain.events;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ibm.gse.eda.vaccines.domain.VaccineOrderEntity;
import io.debezium.outbox.quarkus.ExportedEvent;

@Entity
public class OrderUpdatedEvent implements ExportedEvent<String, String> {
    
    private static ObjectMapper mapper = new ObjectMapper();
    @Id
    public long id;
    @Column(length=2046)
    public String order;
    public Instant timestamp;
    
    public OrderUpdatedEvent(){}

    public OrderUpdatedEvent(long id, JsonNode order) {
        this.id = id;
        try {
            this.order = mapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            this.order = "";
        }
        this.timestamp = Instant.now();
    }

    public static OrderUpdatedEvent of(VaccineOrderEntity order) {
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
        return new OrderUpdatedEvent(order.id, asJson);
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
        return order;
    }

    @Override
    public Instant getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String getType() {
        return "OrderUpdated";
    }
}
