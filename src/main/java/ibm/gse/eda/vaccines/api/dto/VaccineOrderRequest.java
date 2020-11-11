package ibm.gse.eda.vaccines.api.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import ibm.gse.eda.vaccines.domain.OrderStatus;
import ibm.gse.eda.vaccines.domain.VaccineOrderEntity;

@Schema(name = "VaccineOrderRequest", description = "Vaccine order to be delivered in a state or a country")
public class VaccineOrderRequest {
    @Schema(required = false, description = "Unique order identifier, will be generated")
    public Long id;
    @Schema(required = true, description = "Name of the asking organization")
    public String askingOrganization;
    @Schema(required = true, description = "Location for vaccine lots to be delivered")
    public String deliveryLocation;
    @Schema(required = false,
            description = "Higher priority order may displace lower priority order plans",
            enumeration = {"0","1","2","3","5"},
            defaultValue = "0")
    public Integer priority = 0;
    @Schema(required = true, description = "Expected quantity of vaccines")
    public Long quantity; 
    @Schema(required = false, description = "if not provided, indicating ASAP")
    public LocalDate deliveryDate;
    @Schema(required = false, description = "Vaccine type, default COVID-19")
    public String vaccineType = "COVID-19";
    public OrderStatus status;

    public VaccineOrderRequest(){}

    public VaccineOrderRequest(Long idIn, 
            String deliveryLocation,
            String  askingOrganization,
            Long quantity, 
            Integer priority, 
            LocalDate delivery,
            String type, 
            OrderStatus status) {
        this.id = idIn;
        this.askingOrganization = askingOrganization;
        this.deliveryLocation = deliveryLocation;
        this.quantity = quantity;
        this.priority = priority;
        this.deliveryDate = delivery;
        this.vaccineType = type;
        this.status = status;
    }


	public static List<VaccineOrderRequest> fromAll(List<VaccineOrderEntity> orderEntities) {
        if (orderEntities == null) return null;
        List<VaccineOrderRequest> orders = new ArrayList<VaccineOrderRequest>(orderEntities.size());
        for (VaccineOrderEntity voe : orderEntities){
            orders.add(VaccineOrderRequest.fromOrder(voe));
        }
        return orders;
	}

	public static VaccineOrderEntity toOrderEntity(VaccineOrderRequest order) {
        VaccineOrderEntity entity = new VaccineOrderEntity();
        if (order.id != null) {
            entity.id = order.id;
        }
        entity.deliveryLocation = order.deliveryLocation;
        entity.askingOrganization = order.askingOrganization;
        entity.vaccineType = order.vaccineType;
        entity.quantity = order.quantity;
        entity.priority = order.priority;
        entity.deliveryDate = order.deliveryDate;
        entity.status = order.status;
		return entity;
	}

	public static VaccineOrderRequest fromOrder(VaccineOrderEntity entity) {
        VaccineOrderRequest vo = new VaccineOrderRequest(entity.id, 
                entity.deliveryLocation, 
                entity.askingOrganization, 
                entity.quantity,
                entity.priority, 
                entity.deliveryDate, 
                entity.vaccineType,
                entity.status);
		return vo;
	}

}