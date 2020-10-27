package ibm.gse.eda.vaccines.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ibm.gse.eda.vaccines.api.dto.VaccineOrder;
import ibm.gse.eda.vaccines.domain.OrderStatus;
import ibm.gse.eda.vaccines.domain.VaccineOrderEntity;
import io.quarkus.panache.common.Sort;

@Path("/api/v1/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
 
    @GET
    public List<VaccineOrder> getActiveOrders() {
        return VaccineOrder.fromAll(VaccineOrderEntity.listAll(Sort.by("deliveryLocation")));
    }
    
  /**
     * Find an entity of this type by ID.
     *
     * @param id the ID of the entity to find.
     * @return the entity found
    */
    @GET
    @Path("/{id}")
    public VaccineOrder get(@PathParam("id") Long id) {
        VaccineOrderEntity order = VaccineOrderEntity.findById(id);
        if (order == null) {
            throw new WebApplicationException("Order with id of " + id + " does not exist.", 404);
     
        }
        return VaccineOrder.fromEntity(order);
    }
    
    @POST
    @Transactional
    public Response create(VaccineOrder order) {
        validateOrder(order);
        VaccineOrderEntity orderEntity = VaccineOrder.toEntity(order);
        orderEntity.status= OrderStatus.OPEN;
        orderEntity.creationDate = simpleDateFormat.format(new Date());
        orderEntity.persist();
        return Response.ok(order).status(201).build();
    }

    private void validateOrder(VaccineOrder order) {
        if (order.deliveryLocation == null) {
            throw new WebApplicationException("Order deliveryLocation was not set on request.", 422);
        }
        if (order.quantity == null || order.quantity == 0) {
            throw new WebApplicationException("Order quantity was not set on request.", 422);
        }
    }
    @PUT
    @Path("/{id}")
    @Transactional
    public VaccineOrder update(@PathParam("id") String id, VaccineOrder order) {
        if (order.id == null) {
            throw new WebApplicationException("Order is unknown for this id " + order.id, 422);
        }
        validateOrder(order);
        VaccineOrderEntity orderEntity = VaccineOrderEntity.findById(order.id);
        if (orderEntity == null) {
            throw new WebApplicationException("Order with id of " + id + " does not exist.", 404);
        }
        orderEntity.deliveryLocation = order.deliveryLocation;
        orderEntity.quantity = order.quantity;
        orderEntity.priority = order.priority;
        orderEntity.deliveryDate = order.deliveryDate;
        return order;
    }


    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") String id) {
        VaccineOrderEntity orderEntity = VaccineOrderEntity.findById(id);
        if (orderEntity == null) {
            throw new WebApplicationException("Order with id of " + id + " does not exist.", 404);
        }
        orderEntity.delete();
        return Response.status(204).build();
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