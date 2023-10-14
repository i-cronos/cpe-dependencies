package pe.ibk.cpe.dependencies.infrastructure.security.token;

import lombok.Getter;
import lombok.Setter;
import pe.ibk.cpe.dependencies.common.exception.BaseException;
import pe.ibk.cpe.dependencies.common.exception.InfrastructureException;

import java.util.List;

@Getter
@Setter
public class TokenConfiguration {

    private String key;
    private String issuer;
    private List<Config> configs;

    @Getter
    @Setter
    public static class Config {
        private TokenType tokenType;
        private Integer ttl;
        private Integer refreshTtl;
        private String subject;
    }

    public Config solve(TokenType tokenType) {
        return configs.stream()
                .filter(config -> config.tokenType.equals(tokenType))
                .findFirst()
                .orElseThrow(() -> new InfrastructureException(BaseException.Error.builder()
                        .systemMessage("Not found token config : " + tokenType)
                        .userMessage("Not found token config")
                        .build()));
    }
}
