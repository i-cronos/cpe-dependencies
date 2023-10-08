package pe.ibk.cpe.dependencies.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.ibk.cpe.dependencies.global.util.JsonLogUtil;

@Configuration
public class DependenciesGlobalConfiguration {

    @Bean
    public JsonLogUtil jsonLogUtil() {
        return new JsonLogUtil();
    }
}
