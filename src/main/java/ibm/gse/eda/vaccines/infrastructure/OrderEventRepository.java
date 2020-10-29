package ibm.gse.eda.vaccines.infrastructure;

import javax.enterprise.context.ApplicationScoped;

import ibm.gse.eda.vaccines.domain.OrderEvent;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class OrderEventRepository implements PanacheRepository<OrderEvent> {
    
}
