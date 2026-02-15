package io.helidon.examples.tax;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

@ApplicationScoped
@ApplicationPath("/")
@LoginConfig(authMethod = "MP-JWT", realmName = "helidon")
public class TaxApplication extends Application {
}
