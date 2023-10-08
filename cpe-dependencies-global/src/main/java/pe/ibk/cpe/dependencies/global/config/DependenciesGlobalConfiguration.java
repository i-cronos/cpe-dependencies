package pe.ibk.cpe.dependencies.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.ibk.cpe.dependencies.global.jwt.JwtProvider;
import pe.ibk.cpe.dependencies.global.util.CoreJsonUtil;

@Configuration
public class DependenciesGlobalConfiguration {

    @Bean
    public CoreJsonUtil jsonLogUtil() {
        return new CoreJsonUtil();
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider();
    }
}
