package ibm.gse.eda.vaccines.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

import ibm.gse.eda.vaccines.infrastructure.OrderEventRepository;
import ibm.gse.eda.vaccines.infrastructure.OrderRepository;

/**
 * Service to implement business logic to manage order
 */
@ApplicationScoped
public class OrderService {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

    @Inject
    OrderRepository orderRepository;

    @Inject
    OrderEventRepository orderEventRepository;
    
    public OrderService() {
    }

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public VaccineOrderEntity createOrder(VaccineOrderEntity orderEntity) {
        orderEntity.status = OrderStatus.OPEN;
        orderEntity.creationDate = simpleDateFormat.format(new Date());
        OrderEvent evt = OrderEvent.fromEntity(orderEntity);
        evt.type = OrderEvent.ORDER_CREATED;
        orderEntity.setOrderEvent(evt);
        orderEntity = orderRepository.save(orderEntity);
        return orderEntity;
    }

    public List<VaccineOrderEntity> findActive() {
        return null;
    }

    @Transactional
    public VaccineOrderEntity update(VaccineOrderEntity order) {
        VaccineOrderEntity orderEntity = orderRepository.findById(order.id);
        if (orderEntity == null) {
            throw new WebApplicationException("Order with id of " + order.id + " does not exist.", 404);
        }
        orderEntity.deliveryLocation = order.deliveryLocation;
        orderEntity.quantity = order.quantity;
        orderEntity.priority = order.priority;
        orderEntity.deliveryDate = order.deliveryDate;
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