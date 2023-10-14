package pe.ibk.cpe.dependencies.infrastructure.security.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Builder;
import lombok.Getter;
import pe.ibk.cpe.dependencies.common.exception.BaseException;
import pe.ibk.cpe.dependencies.common.exception.DependencyException;
import pe.ibk.cpe.dependencies.infrastructure.security.token.claim.TokenClaimId;
import pe.ibk.cpe.dependencies.infrastructure.security.token.configuration.TokenGeneralConfiguration;
import pe.ibk.cpe.dependencies.infrastructure.security.token.types.TokenType;

import java.util.*;

public class TokenValidationService {
    private final TokenGeneralConfiguration tokenGeneralConfiguration;

    public TokenValidationService(TokenGeneralConfiguration tokenGeneralConfiguration) {
        this.tokenGeneralConfiguration = tokenGeneralConfiguration;
    }

    public TokenValidationResponse validate(TokenValidationRequest tokenValidationRequest) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(tokenGeneralConfiguration.getKey());

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(tokenGeneralConfiguration.getIssuer())
                    .build();

            DecodedJWT jwt = verifier.verify(tokenValidationRequest.getToken());

            return TokenValidationResponse.builder()
                    .tokenType(TokenType.valueOf(jwt.getClaim(TokenClaimId.TYPE).asString()))
                    .credentialId(jwt.getClaim(TokenClaimId.ID).asString())
                    .authorities(arrayToList(jwt.getClaim(TokenClaimId.AUTHORITIES).asArray(String.class)))
                    .data(jwt.getClaim(TokenClaimId.DATA).asMap())
                    .build();


        } catch (Exception ex) {
            throw new DependencyException(BaseException.Error.builder()
                    .systemMessage("Error in token validation : " + ex.getMessage())
                    .userMessage("Error in token validation")
                    .build());
        }
    }

    private List<String> arrayToList(String[] authorities) {
        if (Objects.nonNull(authorities)) {
            return Arrays.asList(authorities);
        }
        return Collections.emptyList();
    }

    @Getter
    @Builder
    public static class TokenValidationRequest {
        private String token;
    }

    @Getter
    @Builder
    public static class TokenValidationResponse {
        private TokenType tokenType;
        private String credentialId;
        private List<String> authorities;
        private Map<String, Object> data;
    }
}
