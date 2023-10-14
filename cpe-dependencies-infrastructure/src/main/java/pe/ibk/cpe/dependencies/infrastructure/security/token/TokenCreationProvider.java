package pe.ibk.cpe.dependencies.infrastructure.security.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Builder;
import lombok.Getter;
import pe.ibk.cpe.dependencies.common.exception.BaseException;
import pe.ibk.cpe.dependencies.common.exception.DependencyException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TokenCreationProvider {
    private final TokenConfiguration tokenConfiguration;

    public TokenCreationProvider(TokenConfiguration tokenConfiguration) {
        this.tokenConfiguration = tokenConfiguration;
    }

    public TokenCreationResponse create(TokenCreationRequest tokenCreationRequest) {
        try {
            TokenConfiguration.Config config = tokenConfiguration.solve(tokenCreationRequest.tokenType);

            Algorithm algorithm = Algorithm.HMAC512(tokenConfiguration.getKey());
            Instant now = Instant.now();
            String token = JWT.create()
                    .withIssuer(tokenConfiguration.getIssuer())
                    .withSubject(config.getSubject())
                    .withClaim(TokenClaimId.TYPE, tokenCreationRequest.getTokenType().name())
                    .withClaim(TokenClaimId.ID, tokenCreationRequest.getCredentialId())
                    .withArrayClaim(TokenClaimId.AUTHORITIES, listToArray(tokenCreationRequest.getAuthorities()))
                    .withClaim(TokenClaimId.DATA, tokenCreationRequest.getData())
                    .withIssuedAt(now)
                    .withExpiresAt(now.plus(config.getTtl(), ChronoUnit.MINUTES))
                    .sign(algorithm);


            return TokenCreationResponse.builder()
                    .tokenType(tokenCreationRequest.getTokenType())
                    .token(token)
                    .build();

        } catch (Exception ex) {
            throw new DependencyException(BaseException.Error.builder()
                    .systemMessage("Error in token type " + tokenCreationRequest.tokenType + " creation : " + ex.getMessage())
                    .userMessage("Error in token creation")
                    .build());
        }
    }

    private String[] listToArray(List<String> authorities) {
        if (Objects.nonNull(authorities))
            return authorities.toArray(String[]::new);
        return new String[]{};
    }


    @Getter
    @Builder
    public static class TokenCreationRequest {
        private TokenType tokenType;
        private String credentialId;
        private List<String> authorities;
        private Map<String, String> data;
    }

    @Getter
    @Builder
    public static class TokenCreationResponse {
        private TokenType tokenType;
        private String token;
    }
}
