package pe.ibk.cpe.dependencies.infrastructure.security.token.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("token-general.security.config")
public class TokenGeneralConfiguration {
    private String issuer;
    private String key;
}
