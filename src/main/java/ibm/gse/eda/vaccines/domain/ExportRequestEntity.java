package ibm.gse.eda.vaccines.domain;

import javax.persistence.Entity;

import ibm.gse.eda.vaccines.api.dto.RequestStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class ExportRequestEntity extends PanacheEntity {
    public Long orderID;
    public Boolean accepted;
    public String signatureDate;
    public String emittedDate;
    public RequestStatus status;
    public String signatureAutority;
    public String country;
}