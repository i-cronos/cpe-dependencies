package pe.ibk.cpe.dependencies.global.exception.error;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserError {
    private String message;
    private String errorCode;
    private String status;
}
