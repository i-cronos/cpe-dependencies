package pe.ibk.cpe.dependencies.infrastructure.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.ibk.cpe.dependencies.common.util.JsonUtil;
import pe.ibk.cpe.dependencies.infrastructure.security.filter.PrivateAppFilter;
import pe.ibk.cpe.dependencies.infrastructure.security.filter.ProtectedAppFilter;
import pe.ibk.cpe.dependencies.infrastructure.security.filter.PublicAppFilter;
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
    public PublicAppFilter publicAppFilter() {
        return new PublicAppFilter();
    }

    @Bean
    public ProtectedAppFilter protectedAppFilter(TokenValidationService tokenValidationService, JsonUtil jsonUtil) {
        return new ProtectedAppFilter(tokenValidationService, jsonUtil);
    }

    @Bean
    public PrivateAppFilter privateAppFilter(TokenValidationService tokenValidationService, JsonUtil jsonUtil) {
        return new PrivateAppFilter(tokenValidationService, jsonUtil);
    }

    @Bean
    public AppSecurityConfiguration appSecurityConfiguration() {
        return new AppSecurityConfiguration();
    }

    @Bean
    public SecurityFilterChain publicSecurityFilterChain(HttpSecurity httpSecurity,
                                                         AppSecurityConfiguration appSecurityConfiguration,
                                                         PublicAppFilter publicAppFilter) throws Exception {
        return httpSecurity
                .securityMatcher(appSecurityConfiguration.getPath().getPublicPath())
                .csrf(csrfConf -> csrfConf.disable())
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(basicConfig -> basicConfig.disable())
                .addFilterAfter(publicAppFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(appSecurityConfiguration.getPath().getPublicPath()).authenticated();
                })
                .build();
    }

    @Bean
    public SecurityFilterChain protectedSecurityFilterChain(HttpSecurity httpSecurity,
                                                            AppSecurityConfiguration appSecurityConfiguration,
                                                            ProtectedAppFilter protectedAppFilter) throws Exception {
        return httpSecurity
                .securityMatcher(appSecurityConfiguration.getPath().getProtectedPath())
                .csrf(csrfConf -> csrfConf.disable())
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(basicConfig -> basicConfig.disable())
                .addFilterAfter(protectedAppFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(appSecurityConfiguration.getPath().getProtectedPath()).authenticated();
                })
                .build();
    }

    @Bean
    public SecurityFilterChain privateSecurityFilterChain(HttpSecurity httpSecurity,
                                                          AppSecurityConfiguration appSecurityConfiguration,
                                                          PrivateAppFilter privateAppFilter) throws Exception {
        return httpSecurity
                .securityMatcher(appSecurityConfiguration.getPath().getPrivatePath())
                .csrf(csrfConf -> csrfConf.disable())
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(basicConfig -> basicConfig.disable())
                .addFilterAfter(privateAppFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(appSecurityConfiguration.getPath().getPrivatePath()).authenticated();
                })
                .build();
    }

}

