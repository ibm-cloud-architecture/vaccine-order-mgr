package ibm.gse.eda.vaccines.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;

import ibm.gse.eda.vaccines.domain.OrderEvent;
import ibm.gse.eda.vaccines.infrastructure.OrderEventRepository;
import io.smallrye.mutiny.Multi;

/**
 * This class is for demo purpose only to validate easily events are created 
 * into the DB table. 
 * Events are published to kafka via CDC
 */
@Path("/api/v1/orderevents")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderEventResource {
    

    @Inject
    OrderEventRepository orderEventRepository;
    
    @GET
    public Multi<OrderEvent> getAllOrderEvents() {
        return Multi.createFrom().items(orderEventRepository.listAll().stream());
    }
}
