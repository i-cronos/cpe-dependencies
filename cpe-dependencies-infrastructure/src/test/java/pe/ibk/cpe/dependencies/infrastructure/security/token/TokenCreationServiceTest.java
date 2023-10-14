package pe.ibk.cpe.dependencies.infrastructure.security.token;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pe.ibk.cpe.dependencies.infrastructure.security.token.configuration.TokenGeneralConfiguration;
import pe.ibk.cpe.dependencies.infrastructure.security.token.configuration.TokenGroupConfiguration;
import pe.ibk.cpe.dependencies.infrastructure.security.token.types.TokenType;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Slf4j
class TokenCreationServiceTest {


    @Test
    void givenTokenCreationRequest_whenCreate_thenResponseToken() {
        TokenCreationService tokenCreationService = buildTokenCreationService();

        TokenCreationService.TokenCreationRequest tokenCreationRequest = TokenCreationService.TokenCreationRequest.builder()
                .tokenType(TokenType.USER)
                .authorities(Arrays.asList("ADMIN", "GUEST"))
                .credentialId(UUID.randomUUID().toString())
                .data(Collections.singletonMap("MODULE_A", "true"))
                .build();

        TokenCreationService.TokenCreationResponse tokenCreationResponse = tokenCreationService.create(tokenCreationRequest);

        log.info("Token creation : {} ", tokenCreationResponse.getToken());

        Assertions.assertNotNull(tokenCreationResponse.getToken());
    }

    private TokenCreationService buildTokenCreationService() {
        TokenGroupConfiguration.Group group = new TokenGroupConfiguration.Group();
        group.setTokenType(TokenType.USER);
        group.setTtl(5);
        group.setRefreshTtl(10);
        group.setSubject("USER");
        TokenGroupConfiguration tokenGroupConfiguration = new TokenGroupConfiguration();
        tokenGroupConfiguration.setGroups(Collections.singletonList(group));

        TokenGeneralConfiguration tokenGeneralConfiguration = new TokenGeneralConfiguration();
        tokenGeneralConfiguration.setKey("LostToken!3.141516");
        tokenGeneralConfiguration.setIssuer("CPE");

        return new TokenCreationService(tokenGeneralConfiguration, tokenGroupConfiguration);
    }
}