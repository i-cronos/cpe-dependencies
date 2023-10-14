package pe.ibk.cpe.dependencies.infrastructure.security.filter.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("app.security.config")
public class AppSecurityConfiguration {
    private Path path;

    @Getter
    @Setter
    public static class Path {
        private String publicPath;
        private String protectedPath;
        private String privatePath;
    }
}
