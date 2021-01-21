package ibm.gse.eda.vaccines.api;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.SseElementType;

import ibm.gse.eda.vaccines.api.dto.ShipmentPlan;
import ibm.gse.eda.vaccines.domain.ShipmentPlanProcessor;
import io.smallrye.mutiny.Multi;


@Path("api/v1/shipments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ShipmentPlanResource {
    
    @Inject
    protected ShipmentPlanProcessor shipmentPlan;
    //Publisher<ShipmentPlan> shipmentPlan;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Multi<ShipmentPlan> getAllPlans(){
        return shipmentPlan.stream();
    }

}
