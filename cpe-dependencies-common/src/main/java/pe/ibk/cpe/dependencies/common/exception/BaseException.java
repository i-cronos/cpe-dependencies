package pe.ibk.cpe.dependencies.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
public class BaseException extends RuntimeException {
    private final Error error;

    public BaseException(Error error) {
        super(error.systemMessage);
        this.error = error;
    }

    @Getter
    @Builder
    @ToString
    public static class Error {
        private String systemMessage;
        private String userMessage;
        private String contextMessage;
        private String errorCode;
        private String groupCode;
    }
}
