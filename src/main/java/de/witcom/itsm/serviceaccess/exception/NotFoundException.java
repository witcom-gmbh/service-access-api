package de.witcom.itsm.serviceaccess.exception;

public class NotFoundException extends BaseException {

    private static final long serialVersionUID = 38816005992204401L;

    public NotFoundException(String message) {
        super(message, ErrorCode.OBJECT_NOT_FOUND);
    }
}