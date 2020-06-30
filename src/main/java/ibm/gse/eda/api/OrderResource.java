package ibm.gse.eda.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import ibm.gse.eda.api.dto.VaccineOrder;
import ibm.gse.eda.vaccines.domain.VaccineOrderEntity;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    @GET
    public List<VaccineOrder> getActiveOrders() {
        return VaccineOrder.fromAll(VaccineOrderEntity.listAll());
    }
  /**
     * Find an entity of this type by ID.
     *
     * @param id the ID of the entity to find.
     * @return the entity found
    */
    @GET
    @Path("/{id}")
    public VaccineOrder get(@PathParam("id") String id) {
        return VaccineOrder.fromEntity(VaccineOrderEntity.findById(new ObjectId(id)));
    }
    
    @POST
    public Response create(VaccineOrder order) {
        VaccineOrderEntity orderEntity = VaccineOrder.toEntity(order);
        orderEntity.persist();
        return Response.status(201).build();
    }

    @PUT
    @Path("/{id}")
    public void update(@PathParam("id") String id, VaccineOrder order) {
        VaccineOrderEntity orderEntity = VaccineOrder.toEntity(order);
        orderEntity.update();
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id) {
        VaccineOrderEntity orderEntity = VaccineOrderEntity.findById(new ObjectId(id));
        orderEntity.delete();
    }

    @GET
    @Path("/search/{destination}")
    public VaccineOrder search(@PathParam("destination") String destination) {
        return VaccineOrder.fromEntity(VaccineOrderEntity.findByDestination(destination));
    }

    @GET
    @Path("/count")
    public Long count() {
        return VaccineOrderEntity.count();
    }
   
}