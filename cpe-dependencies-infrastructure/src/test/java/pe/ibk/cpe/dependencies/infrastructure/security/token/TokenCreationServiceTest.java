package pe.ibk.cpe.dependencies.infrastructure.security.token;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        TokenConfiguration.Custom config = new TokenConfiguration.Custom();
        config.setTokenType(TokenType.USER);
        config.setTtl(5);
        config.setRefreshTtl(10);
        config.setSubject("USER");

        TokenConfiguration tokenConfiguration = new TokenConfiguration();
        tokenConfiguration.setGeneral(new TokenConfiguration.General());
        tokenConfiguration.getGeneral().setKey("LostToken!3.141516");
        tokenConfiguration.getGeneral().setIssuer("CPE");
        tokenConfiguration.setCustoms(Collections.singletonList(config));

        return new TokenCreationService(tokenConfiguration);
    }
}