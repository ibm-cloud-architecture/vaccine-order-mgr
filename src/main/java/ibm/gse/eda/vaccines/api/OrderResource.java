package ibm.gse.eda.vaccines.api;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
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
import ibm.gse.eda.vaccines.domain.OrderService;
import ibm.gse.eda.vaccines.domain.OrderStatus;
import ibm.gse.eda.vaccines.domain.VaccineOrderEntity;
import ibm.gse.eda.vaccines.infrastructure.OrderRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("/api/v1/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
 
    @Inject
    OrderRepository orderRepository;

    @Inject
    OrderService  orderService;

    @GET
    public Multi<VaccineOrder> getAllOrders() {
        return Multi.createFrom().items(VaccineOrder.fromAll(orderRepository.listAll()).stream());
    }

    @GET
    @Path("/active")
    public Multi<VaccineOrder> getActiveOrders() {
        return Multi.createFrom().items(VaccineOrder.fromAll(orderService.findActive()).stream());
    }
    
  /**
     * Find an entity of this type by ID.
     *
     * @param id the ID of the entity to find.
     * @return the entity found
    */
    @GET
    @Path("/{id}")
    public Uni<VaccineOrder> get(@PathParam("id") Long id) {
        VaccineOrderEntity order = orderRepository.findById(id);
        if (order == null) {
            throw new WebApplicationException("Order with id of " + id + " does not exist.", 404);
     
        }
        return Uni.createFrom().item(VaccineOrder.fromEntity(order));
    }
    
    @POST
    public Uni<VaccineOrder> create(VaccineOrder order) {
        validateOrder(order);
        VaccineOrderEntity orderEntity = orderService.createOrder(VaccineOrder.toEntity(order));
        return Uni.createFrom().item(VaccineOrder.fromEntity(orderEntity));
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
    public Uni<VaccineOrder> update(@PathParam("id") String id, VaccineOrder order) {
        if (order.id == null) {
            throw new WebApplicationException("Order is unknown for this id " + order.id, 422);
        }
        validateOrder(order);
        VaccineOrderEntity orderEntity = orderService.update(VaccineOrder.toEntity(order));
        
        return Uni.createFrom().item(VaccineOrder.fromEntity(orderEntity));
    }


    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        orderService.deleteOrder(id);
        return Response.status(204).build();
    }

    @GET
    @Path("/search/{destination}")
    public VaccineOrder search(@PathParam("destination") String destination) {
        return VaccineOrder.fromEntity(orderRepository.findByDestination(destination));
    }

}