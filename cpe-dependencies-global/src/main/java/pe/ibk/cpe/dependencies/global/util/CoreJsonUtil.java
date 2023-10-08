package pe.ibk.cpe.dependencies.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CoreJsonUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String toJson(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            log.info("Error json util parse: {}", ex.getMessage());
            return null;
        }
    }
}
