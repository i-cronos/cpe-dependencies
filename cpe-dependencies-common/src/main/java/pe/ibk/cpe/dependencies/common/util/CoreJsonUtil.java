package pe.ibk.cpe.dependencies.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public final class CoreJsonUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String toJson(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            log.info("Error json util parse to String: {}", ex.getMessage());
            return null;
        }
    }

    public <T> T fromInputStream(InputStream json, Class<T> classType) {
        try {
            return objectMapper.readValue(json, classType);
        } catch (Exception ex) {
            log.info("Error json util parse to Object: {}", ex.getMessage());
            return null;
        }
    }
}
