package sunnn.filehub.config;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import sunnn.filehub.util.FileHubProperties;

@Component
@DependsOn("fileHubProperties")
public class PortConfiguration implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        factory.setPort(FileHubProperties.port);
    }
}
