package ut;

import com.google.inject.Inject;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import ibm.gse.eda.vaccines.domain.OrderService;
import ibm.gse.eda.vaccines.domain.VaccineOrderEntity;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class OrderServiceTest {
    
    OrderService orderService;

    @Test
    public void whenPostNewOrder_thenOrderShouldBeFound(){
        orderService = new OrderService(new TestOrderRepository());
        VaccineOrderEntity orderEntity = new VaccineOrderEntity("Oregon","Portland",20);
        VaccineOrderEntity orderEntityOut = orderService.saveNewOrder(orderEntity);
        Assertions.assertNotNull(orderEntityOut);
        orderEntityOut = orderService.findByOrganization("Oregon");
        Assertions.assertNotNull(orderEntityOut);
    }
}
