package pe.ibk.cpe.dependencies.application.advice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import pe.ibk.cpe.dependencies.common.exception.error.SystemError;
import pe.ibk.cpe.dependencies.common.exception.error.UserError;
import pe.ibk.cpe.dependencies.common.exception.DomainException;

import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
public class ApplicationControllerAdvice {

    @ExceptionHandler({DomainException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public UserError handleDomainError(DomainException exception, WebRequest webRequest) {
        SystemError systemError = SystemError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .timestamp(LocalDateTime.now().toString())
                .path(webRequest.getContextPath())
                .userMessage(exception.getError().getUserMessage())
                .systemMessage(exception.getError().getSystemMessage())
                .contextMessage(exception.getError().getContextMessage())
                .errorCode(exception.getError().getErrorCode())
                .groupCode(exception.getError().getGroupCode())
                .build();

        log.info("Domain error : {}", systemError);

        return map(systemError);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public UserError handleGeneralError(Exception exception, WebRequest webRequest) {
        SystemError systemError = SystemError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .timestamp(LocalDateTime.now().toString())
                .path(webRequest.getContextPath())
                .userMessage("General error")
                .systemMessage(exception.getMessage())
                .build();

        log.info("General Error : class {}, {}", exception.getClass(), systemError);

        return map(systemError);
    }

    private UserError map(SystemError systemError) {
        return UserError.builder()
                .message(systemError.getUserMessage())
                .errorCode(systemError.getErrorCode())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .build();
    }

}
