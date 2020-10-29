package ut;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import ibm.gse.eda.vaccines.domain.VaccineOrderEntity;
import ibm.gse.eda.vaccines.infrastructure.OrderRepository;

@Priority(1)
@Alternative
@ApplicationScoped
public class TestOrderRepository extends OrderRepository {
    
    @PostConstruct
    public void init() {
        persist(new VaccineOrderEntity("France","Paris",100),
            new VaccineOrderEntity("California","Los Angeles",100));
    }
}
