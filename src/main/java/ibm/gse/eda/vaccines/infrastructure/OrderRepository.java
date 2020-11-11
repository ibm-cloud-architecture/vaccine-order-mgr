package ibm.gse.eda.vaccines.infrastructure;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import ibm.gse.eda.vaccines.domain.OrderStatus;
import ibm.gse.eda.vaccines.domain.VaccineOrderEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<VaccineOrderEntity> {
    
    public VaccineOrderEntity findByDestination(String destination) {
		return find("deliveryLocation", destination).firstResult();
    }
    
    public List<VaccineOrderEntity> findActive(){
        return list("status",OrderStatus.BUILDING );
    }

    
    public VaccineOrderEntity save(VaccineOrderEntity e) {
        persist(e);
        return e;
    }
    
}
