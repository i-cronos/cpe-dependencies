package pe.ibk.cpe.dependencies.common.exception;

import lombok.Getter;

@Getter
public class DomainException extends BaseException {

    public DomainException(Error error) {
        super(error);
    }

}
