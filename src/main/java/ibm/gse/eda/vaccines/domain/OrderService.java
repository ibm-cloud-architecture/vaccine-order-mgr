package ibm.gse.eda.vaccines.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

import org.jboss.logging.Logger;

import ibm.gse.eda.vaccines.domain.events.OrderCreatedEvent;
import ibm.gse.eda.vaccines.domain.events.OrderUpdatedEvent;
import ibm.gse.eda.vaccines.infrastructure.OrderRepository;
import io.debezium.outbox.quarkus.ExportedEvent;

/**
 * Service to implement business logic to manage order
 */
@ApplicationScoped
public class OrderService {
    Logger logger = Logger.getLogger(OrderService.class);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

    @Inject
    OrderRepository orderRepository;

    @Inject
    Event<ExportedEvent<?, ?>> event;
  
    public OrderService() {
    }

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public VaccineOrderEntity saveNewOrder(VaccineOrderEntity orderEntity) {
        logger.info("persist for " + orderEntity.askingOrganization + " " + orderEntity.id);
        orderEntity.status = OrderStatus.OPEN;
        orderEntity.creationDate = simpleDateFormat.format(new Date());
        orderRepository.persist(orderEntity);
        event.fire(OrderCreatedEvent.of(orderEntity));
        return orderEntity;
    }

    public List<VaccineOrderEntity> findActive() {
        return null;
    }

    @Transactional
    public VaccineOrderEntity updateExistingOrder(VaccineOrderEntity order) {
        VaccineOrderEntity orderEntity = orderRepository.findById(order.id);
        if (orderEntity == null) {
            throw new WebApplicationException("Order with id of " + order.id + " does not exist.", 404);
        }
        orderEntity.deliveryLocation = order.deliveryLocation;
        orderEntity.quantity = order.quantity;
        orderEntity.priority = order.priority;
        orderEntity.deliveryDate = order.deliveryDate;

        event.fire(OrderUpdatedEvent.of(orderEntity));
        orderRepository.persistAndFlush(orderEntity);
        return orderEntity;
    }

    @Transactional
    public void deleteOrder(String id) {
        VaccineOrderEntity orderEntity = VaccineOrderEntity.findById(id);
        if (orderEntity == null) {
            throw new WebApplicationException("Order with id of " + id + " does not exist.", 404);
        }
        orderRepository.delete("id", id);
    }

	public VaccineOrderEntity findByOrganization(String string) {
		return null;
	}
}