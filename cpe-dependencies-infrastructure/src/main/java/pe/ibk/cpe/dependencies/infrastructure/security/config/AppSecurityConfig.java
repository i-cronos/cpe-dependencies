package pe.ibk.cpe.dependencies.infrastructure.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.ibk.cpe.dependencies.common.util.JsonUtil;
import pe.ibk.cpe.dependencies.infrastructure.security.filter.PublicAppTokenFilter;
import pe.ibk.cpe.dependencies.infrastructure.security.filter.ProtectedAppTokenFilter;
import pe.ibk.cpe.dependencies.infrastructure.security.filter.PrivateAppTokenFilter;
import pe.ibk.cpe.dependencies.infrastructure.security.filter.configuration.AppSecurityConfiguration;
import pe.ibk.cpe.dependencies.infrastructure.security.token.TokenValidationService;
import pe.ibk.cpe.dependencies.infrastructure.security.token.configuration.TokenGeneralConfiguration;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "app.security.config.enabled", havingValue = "true", matchIfMissing = false)
public class AppSecurityConfig {

    @Bean
    public JsonUtil jsonUtil() {
        return new JsonUtil();
    }

    @Bean
    public TokenGeneralConfiguration tokenGeneralConfiguration() {
        return new TokenGeneralConfiguration();
    }

    @Bean
    public TokenValidationService tokenValidationService(TokenGeneralConfiguration tokenGeneralConfiguration) {
        return new TokenValidationService(tokenGeneralConfiguration);
    }

    @Bean
    public AppSecurityConfiguration appSecurityConfiguration() {
        return new AppSecurityConfiguration();
    }


    @Bean
    @Order(100)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity httpSecurity,
                                                      AppSecurityConfiguration appSecurityConfiguration,
                                                      TokenValidationService tokenValidationService,
                                                      JsonUtil jsonUtil) throws Exception {
        ProtectedAppTokenFilter filter = new ProtectedAppTokenFilter(tokenValidationService, jsonUtil);
        return httpSecurity
                .securityMatcher(appSecurityConfiguration.getPath().getProtectedPath())
                .csrf(csrfConf -> csrfConf.disable())
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(basicConfig -> basicConfig.disable())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> {
                    authorize.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    @Order(101)
    public SecurityFilterChain internalSecurityFilterChain(HttpSecurity httpSecurity,
                                                           AppSecurityConfiguration appSecurityConfiguration,
                                                           TokenValidationService tokenValidationService,
                                                           JsonUtil jsonUtil) throws Exception {
        PrivateAppTokenFilter filter = new PrivateAppTokenFilter(tokenValidationService, jsonUtil);

        return httpSecurity
                .securityMatcher(appSecurityConfiguration.getPath().getPrivatePath())
                .csrf(csrfConf -> csrfConf.disable())
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(basicConfig -> basicConfig.disable())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> {
                    authorize.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    @Order(102)
    public SecurityFilterChain publicSecurityFilterChain(HttpSecurity httpSecurity,
                                                         AppSecurityConfiguration appSecurityConfiguration,
                                                         TokenValidationService tokenValidationService,
                                                         JsonUtil jsonUtil) throws Exception {
        PublicAppTokenFilter filter = new PublicAppTokenFilter();

        return httpSecurity
                .securityMatcher(appSecurityConfiguration.getPath().getPublicPath())
                .csrf(csrfConf -> csrfConf.disable())
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(basicConfig -> basicConfig.disable())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> {
                    authorize.anyRequest().permitAll();
                })
                .build();
    }


}

