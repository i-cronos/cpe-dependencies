package pe.ibk.cpe.dependencies.application.error;


public final class ApplicationErrorMapper {

    private ApplicationErrorMapper() {
    }

    public static UserError map(SystemError systemError) {
        return UserError.builder()
                .message(systemError.getUserMessage())
                .errorCode(systemError.getErrorCode())
                .build();
    }
}
