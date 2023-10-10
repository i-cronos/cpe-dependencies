package pe.ibk.cpe.dependencies.common.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    private final Error error;

    public DomainException(Error error) {
        super(error.systemMessage);
        this.error = error;
    }

    @Getter
    @Builder
    public static class Error {
        private String systemMessage;
        private String userMessage;
        private String contextMessage;
        private String errorCode;
        private String groupCode;
    }

}
