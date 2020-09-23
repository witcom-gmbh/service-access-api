package de.witcom.itsm.serviceaccess.exception;

public class BadRequestException extends BaseException {

    private static final long serialVersionUID = 38816005992204401L;

    public BadRequestException(String message) {
        super(message, ErrorCode.BAD_REQUEST);
    }
}