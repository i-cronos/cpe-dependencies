package pe.ibk.cpe.dependencies.application.error;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserError {
    private String message;
    private String errorCode;
}
