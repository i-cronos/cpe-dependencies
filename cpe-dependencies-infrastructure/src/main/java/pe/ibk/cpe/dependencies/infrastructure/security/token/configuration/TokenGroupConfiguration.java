package pe.ibk.cpe.dependencies.infrastructure.security.token.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import pe.ibk.cpe.dependencies.common.exception.BaseException;
import pe.ibk.cpe.dependencies.common.exception.InfrastructureException;
import pe.ibk.cpe.dependencies.infrastructure.security.token.types.TokenType;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties("token-group.security.config")
public class TokenGroupConfiguration {

    private List<Group> groups;

    @Getter
    @Setter
    public static class Group {
        private TokenType tokenType;
        private Integer ttl;
        private Integer refreshTtl;
        private String subject;
    }

    public Group solve(TokenType tokenType) {
        return groups.stream()
                .filter(config -> config.tokenType.equals(tokenType))
                .findFirst()
                .orElseThrow(() -> new InfrastructureException(BaseException.Error.builder()
                        .systemMessage("Not found token config : " + tokenType)
                        .userMessage("Not found token config")
                        .build()));
    }
}
