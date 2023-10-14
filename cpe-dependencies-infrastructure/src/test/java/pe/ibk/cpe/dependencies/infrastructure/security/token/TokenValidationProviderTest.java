package pe.ibk.cpe.dependencies.infrastructure.security.token;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pe.ibk.cpe.dependencies.common.exception.BaseException;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Slf4j
class TokenValidationProviderTest {
    static String KEY = "LostToken!3.141516";

    @Test
    void givenTokenValidationRequest_whenValidate_thenValidateResponse() {
        TokenCreationProvider tokenCreationProvider = buildTokenCreationProvider(KEY);
        TokenValidationProvider tokenValidationProvider = buildTokenValidationProvider(KEY);

        String userId = UUID.randomUUID().toString();

        TokenCreationProvider.TokenCreationRequest tokenCreationRequest = TokenCreationProvider.TokenCreationRequest.builder()
                .tokenType(TokenType.USER)
                .authorities(Arrays.asList("ADMIN", "GUEST"))
                .credentialId(userId)
                .data(Collections.singletonMap("MODULE_A", "true"))
                .build();

        TokenCreationProvider.TokenCreationResponse tokenCreationResponse = tokenCreationProvider.create(tokenCreationRequest);
        log.info("Token creation : {} ", tokenCreationResponse.getToken());
        TokenValidationProvider.TokenValidationRequest tokenValidationRequest = TokenValidationProvider.TokenValidationRequest.builder()
                .token(tokenCreationResponse.getToken())
                .build();

        TokenValidationProvider.TokenValidationResponse tokenValidationResponse = tokenValidationProvider.validate(tokenValidationRequest);

        log.info("Token validation : {} ", tokenValidationResponse.getCredentialId());

        Assertions.assertEquals(userId, tokenValidationResponse.getCredentialId());
    }

    @Test
    void givenTokenValidationRequest_whenValidate_thenExpired() {
        TokenValidationProvider tokenValidationProvider = buildTokenValidationProvider(KEY);

        TokenValidationProvider.TokenValidationRequest tokenValidationRequest = TokenValidationProvider.TokenValidationRequest.builder()
                .token("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJDUEUiLCJzdWIiOiJVU0VSIiwiX3R5cGUiOiJVU0VSIiwiX2lkIjoiMDRhYjE5ZTUtM2E1ZC00YjcwLWE1MDgtZTkyYjU4NGQzZjRjIiwiX2dyYW50cyI6WyJBRE1JTiIsIkdVRVNUIl0sIl9kYXRhIjp7Ik1PRFVMRV9BIjoidHJ1ZSJ9LCJpYXQiOjE2OTcyODQ1MDMsImV4cCI6MTY5NzI4NDgwM30.8ZTYg3GWr2MlEp-Az6rDSa-5G99PD74CdcQhun4FO7s-QQWmFKY22bNEweSF88dHA09t2fTxSZH0NgOqK7Cs1g")
                .build();

        BaseException exception = Assertions.assertThrows(BaseException.class, () -> tokenValidationProvider.validate(tokenValidationRequest));

        System.out.println(exception.getError());
    }

    @Test
    void givenTokenValidationRequest_whenValidate_thenInvalidSignature() {
        TokenCreationProvider tokenCreationProvider = buildTokenCreationProvider(KEY);
        TokenValidationProvider tokenValidationProvider = buildTokenValidationProvider(KEY + "*");

        String userId = UUID.randomUUID().toString();

        TokenCreationProvider.TokenCreationRequest tokenCreationRequest = TokenCreationProvider.TokenCreationRequest.builder()
                .tokenType(TokenType.USER)
                .authorities(Arrays.asList("ADMIN", "GUEST"))
                .credentialId(userId)
                .data(Collections.singletonMap("MODULE_A", "true"))
                .build();

        TokenCreationProvider.TokenCreationResponse tokenCreationResponse = tokenCreationProvider.create(tokenCreationRequest);
        log.info("Token creation : {} ", tokenCreationResponse.getToken());
        TokenValidationProvider.TokenValidationRequest tokenValidationRequest = TokenValidationProvider.TokenValidationRequest.builder()
                .token(tokenCreationResponse.getToken())
                .build();

        BaseException exception = Assertions.assertThrows(BaseException.class, () -> tokenValidationProvider.validate(tokenValidationRequest));

        System.out.println(exception.getError());
    }

    private TokenCreationProvider buildTokenCreationProvider(String key) {
        return new TokenCreationProvider(buildTokenConfiguration(key));
    }

    private TokenValidationProvider buildTokenValidationProvider(String key) {
        return new TokenValidationProvider(buildTokenConfiguration(key));
    }

    private TokenConfiguration buildTokenConfiguration(String key) {
        TokenConfiguration.Config config = new TokenConfiguration.Config();
        config.setTokenType(TokenType.USER);
        config.setTtl(5);
        config.setRefreshTtl(10);
        config.setSubject("USER");

        TokenConfiguration tokenConfiguration = new TokenConfiguration();
        tokenConfiguration.setKey(key);
        tokenConfiguration.setIssuer("CPE");
        tokenConfiguration.setConfigs(Collections.singletonList(config));

        return tokenConfiguration;
    }

}