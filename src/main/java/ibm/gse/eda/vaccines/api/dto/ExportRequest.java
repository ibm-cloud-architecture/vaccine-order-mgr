package ibm.gse.eda.vaccines.api.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Define a vaccine export to the governement agency entity.
 */
@Schema(name = "ExportRequest", description = "Export request for the country the manufacturer resides")
public class ExportRequest {
    public Long id;
    public Long orderID;
    public Boolean accepted;
    public String signatureDate;
    public String emittedDate;
    public RequestStatus status;
    public String signatureAutority;
    public String country;
    
}