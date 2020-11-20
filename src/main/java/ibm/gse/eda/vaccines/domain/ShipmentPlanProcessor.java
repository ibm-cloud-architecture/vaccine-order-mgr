package ibm.gse.eda.vaccines.domain;

import java.util.HashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import ibm.gse.eda.vaccines.api.dto.ShipmentPlan;
import ibm.gse.eda.vaccines.domain.events.ShipmentPlanEvent;
import io.smallrye.mutiny.Multi;

@ApplicationScoped
public class ShipmentPlanProcessor {
    private Logger logger = Logger.getLogger(ShipmentPlanProcessor.class);
    private Jsonb jsonb = JsonbBuilder.create();

    private HashMap<String,ShipmentPlan> plans = new HashMap<String,ShipmentPlan>();
    
    public ShipmentPlanProcessor(){
    }

    @Incoming("shipments")
    public void process(ShipmentPlanEvent evt){
        logger.info("Event received: " + jsonb.toJson(evt));
        plans.put(evt.planID,ShipmentPlan.from(evt));
    }

    public Multi<ShipmentPlan> stream() {
        return Multi.createFrom().items(plans.values().stream());
    }
}
