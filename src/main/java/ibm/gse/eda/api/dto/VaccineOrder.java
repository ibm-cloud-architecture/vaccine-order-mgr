package ibm.gse.eda.api.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import ibm.gse.eda.vaccines.domain.OrderStatus;
import ibm.gse.eda.vaccines.domain.VaccineOrderEntity;

@Schema(name = "VaccineOrder", description = "Vaccine order to be delivered in a state or a country")
public class VaccineOrder {
    @Schema(required = false, description = "Unique order identifier, will be generated")
    public String id;
    @Schema(required = true, description = "Location for vaccine lots to be delivered")
    public String deliveryLocation;
    @Schema(required = false,
            description = " higher priority order may displace lower priority order plans",
            enumeration = {"0","1","2"},
            defaultValue = "0")
    public Integer priority = 0;
    @Schema(required = true, description = "Expected quantity of vaccines")
    public Long quantity; 
    @Schema(required = false, description = "if not provided, indicating ASAP")
    public LocalDate deliveryDate;
    public OrderStatus status;

    public VaccineOrder(){}

    public VaccineOrder(String idIn, String deliveryLocation, Long quantity, Integer p, LocalDate delivery, OrderStatus status) {
        this.id = idIn;
        this.deliveryLocation = deliveryLocation;
        this.quantity = quantity;
        this.priority = p;
        this.deliveryDate = delivery;
        this.status = status;
    }

	public static List<VaccineOrder> fromAll(List<VaccineOrderEntity> orderEntities) {
        if (orderEntities == null) return null;
        List<VaccineOrder> orders = new ArrayList<VaccineOrder>(orderEntities.size());
        for (VaccineOrderEntity voe : orderEntities){
            orders.add(VaccineOrder.fromEntity(voe));
        }
        return orders;
	}

	public static VaccineOrderEntity toEntity(VaccineOrder order) {
        VaccineOrderEntity entity = new VaccineOrderEntity();
        if (order.id != null) {
            entity.id = new ObjectId(order.id);
        }
        entity.deliveryLocation = order.deliveryLocation;
        entity.quantity = order.quantity;
        entity.priority = order.priority;
        entity.deliveryDate = order.deliveryDate;
        entity.status = order.status;
		return entity;
	}

	public static VaccineOrder fromEntity(VaccineOrderEntity entity) {
        VaccineOrder vo = new VaccineOrder(entity.id.toString(), entity.deliveryLocation, entity.quantity,entity.priority, entity.deliveryDate, entity.status);
		return vo;
	}

}