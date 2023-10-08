package pe.ibk.cpe.dependencies.global.util;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

@Slf4j
class CoreJsonUtilTest {
    CoreJsonUtil jsonLogUtil = new CoreJsonUtil();

    @Test
    void givenObject_whenToJson_thenStringJson() {
        ExampleUser exampleUser = ExampleUser.builder()
                .name("Pepito")
                .age(20)
                .build();

        String json = jsonLogUtil.toJson(exampleUser);

        log.info("json : {}", json);

        Assertions.assertEquals("{\"name\":\"Pepito\",\"age\":20}", json);
    }

    @Test
    void givenNullObject_whenToJson_thenStringJson() {
        String json = jsonLogUtil.toJson(null);

        log.info("json : {}", json);

        Assertions.assertNull(null);
    }

    @Getter
    @Builder
    private static class ExampleUser implements Serializable {
        private String name;
        private Integer age;
    }
}