package pe.ibk.cpe.dependencies.common.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pe.ibk.cpe.dependencies.common.util.CoreJsonUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Slf4j
class CoreJsonUtilTest {
    CoreJsonUtil jsonLogUtil = new CoreJsonUtil();

    @Test
    void givenObject_whenToJson_thenStringJson() {
        ExampleUser exampleUser = new ExampleUser("Pepito", 20);

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

    @Test
    void givenInputStreamJson_whenParse_thenValidateResult() throws IOException {
        String json = "{\"name\":\"Benito\",\"age\":20}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        ExampleUser exampleUser = jsonLogUtil.fromInputStream(inputStream, ExampleUser.class);

        inputStream.close();

        log.info("Example user: {}", exampleUser);

        Assertions.assertEquals("Benito", exampleUser.getName());
        Assertions.assertEquals(20, exampleUser.getAge());
    }

    @Getter
    @Setter
    @ToString
    private static class ExampleUser implements Serializable {
        private String name;
        private Integer age;

        public ExampleUser() {
        }

        public ExampleUser(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }
}