package pe.ibk.cpe.dependencies.infrastructure.security.token;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Slf4j
class TokenCreationProviderTest {

    static TokenConfiguration tokenConfiguration;
    static TokenCreationProvider tokenCreationProvider;

    static {
        TokenConfiguration.Config config = new TokenConfiguration.Config();
        config.setTokenType(TokenType.USER);
        config.setTtl(5);
        config.setRefreshTtl(10);
        config.setSubject("USER");

        tokenConfiguration = new TokenConfiguration();
        tokenConfiguration.setKey("LostToken!3.141516");
        tokenConfiguration.setIssuer("CPE");
        tokenConfiguration.setConfigs(Collections.singletonList(config));

        tokenCreationProvider = new TokenCreationProvider(tokenConfiguration);

    }

    @Test
    void givenTokenCreationRequest_whenCreate_thenResponseToken() {
        TokenCreationProvider.TokenCreationRequest tokenCreationRequest = TokenCreationProvider.TokenCreationRequest.builder()
                .tokenType(TokenType.USER)
                .authorities(Arrays.asList("ADMIN", "GUEST"))
                .credentialId(UUID.randomUUID().toString())
                .data(Collections.singletonMap("MODULE_A", "true"))
                .build();

        TokenCreationProvider.TokenCreationResponse tokenCreationResponse = tokenCreationProvider.create(tokenCreationRequest);

        log.info("Token creation : {} ", tokenCreationResponse.getToken());

        Assertions.assertNotNull(tokenCreationResponse.getToken());
    }
}