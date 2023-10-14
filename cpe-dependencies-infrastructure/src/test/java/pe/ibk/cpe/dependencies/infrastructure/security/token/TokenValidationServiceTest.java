package pe.ibk.cpe.dependencies.infrastructure.security.token;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pe.ibk.cpe.dependencies.common.exception.BaseException;
import pe.ibk.cpe.dependencies.infrastructure.security.token.configuration.TokenGeneralConfiguration;
import pe.ibk.cpe.dependencies.infrastructure.security.token.configuration.TokenGroupConfiguration;
import pe.ibk.cpe.dependencies.infrastructure.security.token.types.TokenType;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Slf4j
class TokenValidationServiceTest {
    static String KEY = "LostToken!3.141516";

    @Test
    void givenTokenValidationRequest_whenValidate_thenValidateResponse() {
        TokenCreationService tokenCreationService = buildTokenCreationProvider(KEY);
        TokenValidationService tokenValidationService = buildTokenValidationProvider(KEY);

        String userId = UUID.randomUUID().toString();

        TokenCreationService.TokenCreationRequest tokenCreationRequest = TokenCreationService.TokenCreationRequest.builder()
                .tokenType(TokenType.USER)
                .authorities(Arrays.asList("ADMIN", "GUEST"))
                .credentialId(userId)
                .data(Collections.singletonMap("MODULE_A", "true"))
                .build();

        TokenCreationService.TokenCreationResponse tokenCreationResponse = tokenCreationService.create(tokenCreationRequest);
        log.info("Token creation : {} ", tokenCreationResponse.getToken());
        TokenValidationService.TokenValidationRequest tokenValidationRequest = TokenValidationService.TokenValidationRequest.builder()
                .token(tokenCreationResponse.getToken())
                .build();

        TokenValidationService.TokenValidationResponse tokenValidationResponse = tokenValidationService.validate(tokenValidationRequest);

        log.info("Token validation : {} ", tokenValidationResponse.getCredentialId());

        Assertions.assertEquals(userId, tokenValidationResponse.getCredentialId());
    }

    @Test
    void givenTokenValidationRequest_whenValidate_thenExpired() {
        TokenValidationService tokenValidationService = buildTokenValidationProvider(KEY);

        TokenValidationService.TokenValidationRequest tokenValidationRequest = TokenValidationService.TokenValidationRequest.builder()
                .token("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJDUEUiLCJzdWIiOiJVU0VSIiwiX3R5cGUiOiJVU0VSIiwiX2lkIjoiMDRhYjE5ZTUtM2E1ZC00YjcwLWE1MDgtZTkyYjU4NGQzZjRjIiwiX2dyYW50cyI6WyJBRE1JTiIsIkdVRVNUIl0sIl9kYXRhIjp7Ik1PRFVMRV9BIjoidHJ1ZSJ9LCJpYXQiOjE2OTcyODQ1MDMsImV4cCI6MTY5NzI4NDgwM30.8ZTYg3GWr2MlEp-Az6rDSa-5G99PD74CdcQhun4FO7s-QQWmFKY22bNEweSF88dHA09t2fTxSZH0NgOqK7Cs1g")
                .build();

        BaseException exception = Assertions.assertThrows(BaseException.class, () -> tokenValidationService.validate(tokenValidationRequest));

        System.out.println(exception.getError());
    }

    @Test
    void givenTokenValidationRequest_whenValidate_thenInvalidSignature() {
        TokenCreationService tokenCreationService = buildTokenCreationProvider(KEY);
        TokenValidationService tokenValidationService = buildTokenValidationProvider(KEY + "*");

        String userId = UUID.randomUUID().toString();

        TokenCreationService.TokenCreationRequest tokenCreationRequest = TokenCreationService.TokenCreationRequest.builder()
                .tokenType(TokenType.USER)
                .authorities(Arrays.asList("ADMIN", "GUEST"))
                .credentialId(userId)
                .data(Collections.singletonMap("MODULE_A", "true"))
                .build();

        TokenCreationService.TokenCreationResponse tokenCreationResponse = tokenCreationService.create(tokenCreationRequest);
        log.info("Token creation : {} ", tokenCreationResponse.getToken());
        TokenValidationService.TokenValidationRequest tokenValidationRequest = TokenValidationService.TokenValidationRequest.builder()
                .token(tokenCreationResponse.getToken())
                .build();

        BaseException exception = Assertions.assertThrows(BaseException.class, () -> tokenValidationService.validate(tokenValidationRequest));

        System.out.println(exception.getError());
    }

    private TokenCreationService buildTokenCreationProvider(String key) {
        TokenGroupConfiguration.Group group = new TokenGroupConfiguration.Group();
        group.setTokenType(TokenType.USER);
        group.setTtl(5);
        group.setRefreshTtl(10);
        group.setSubject("USER");
        TokenGroupConfiguration tokenGroupConfiguration = new TokenGroupConfiguration();
        tokenGroupConfiguration.setGroups(Collections.singletonList(group));

        TokenGeneralConfiguration tokenGeneralConfiguration = new TokenGeneralConfiguration();
        tokenGeneralConfiguration.setKey(key);
        tokenGeneralConfiguration.setIssuer("CPE");

        return new TokenCreationService(tokenGeneralConfiguration, tokenGroupConfiguration);
    }

    private TokenValidationService buildTokenValidationProvider(String key) {
        TokenGeneralConfiguration tokenGeneralConfiguration = new TokenGeneralConfiguration();
        tokenGeneralConfiguration.setKey(key);
        tokenGeneralConfiguration.setIssuer("CPE");

        return new TokenValidationService(tokenGeneralConfiguration);
    }

}