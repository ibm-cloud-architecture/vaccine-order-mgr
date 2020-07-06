package ibm.gse.eda.vaccines.api;

import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

@OpenAPIDefinition(
    info = @Info(
        title="Vaccine Order Manager API",
        version = "0.0.1",
        contact = @Contact(
            name = "IBM Garage Solution Engineering",
            url = "http://github.com/ibm-cloud-architecture/vaccine-order-mgr"),
        license = @License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class OrderApplication extends Application {
    
}