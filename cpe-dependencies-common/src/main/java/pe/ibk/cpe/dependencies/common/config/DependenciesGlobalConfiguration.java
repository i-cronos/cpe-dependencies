package pe.ibk.cpe.dependencies.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.ibk.cpe.dependencies.common.jwt.JwtProvider;
import pe.ibk.cpe.dependencies.common.util.CoreJsonUtil;

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
