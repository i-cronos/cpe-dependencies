package pe.ibk.cpe.dependencies.application.error;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SystemError {
    private String userMessage;
    private String systemMessage;
    private String contextMessage;
    private String errorCode;
    private String groupCode;
    private String status;
    private String timestamp;
    private String path;
}
