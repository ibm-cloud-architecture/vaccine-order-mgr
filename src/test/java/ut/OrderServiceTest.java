package ut;

import com.google.inject.Inject;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import ibm.gse.eda.vaccines.domain.OrderService;
import ibm.gse.eda.vaccines.domain.VaccineOrderEntity;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class OrderServiceTest {
    
    @Inject
    OrderService orderService;

    @Test
    public void whenPostNewOrder_thenOrderShouldBeFound(){
        VaccineOrderEntity orderEntity = new VaccineOrderEntity("Oregon","Portland",20);
        VaccineOrderEntity orderEntityOut = orderService.createOrder(orderEntity);
        Assertions.assertNotNull(orderEntityOut);
        orderEntityOut = orderService.findByOrganization("Oregon");
        Assertions.assertNotNull(orderEntityOut);
    }
}
