package pe.ibk.cpe.dependencies.infrastructure.security.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import pe.ibk.cpe.dependencies.common.exception.BaseException;
import pe.ibk.cpe.dependencies.common.exception.InfrastructureException;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties("token.security.config")
public class TokenConfiguration {

    private General general;
    private List<Custom> customs;

    @Getter
    @Setter
    public static class General {
        private String key;
        private String issuer;
    }

    @Getter
    @Setter
    public static class Custom {
        private TokenType tokenType;
        private Integer ttl;
        private Integer refreshTtl;
        private String subject;
    }

    public Custom solve(TokenType tokenType) {
        return customs.stream()
                .filter(config -> config.tokenType.equals(tokenType))
                .findFirst()
                .orElseThrow(() -> new InfrastructureException(BaseException.Error.builder()
                        .systemMessage("Not found token config : " + tokenType)
                        .userMessage("Not found token config")
                        .build()));
    }
}
