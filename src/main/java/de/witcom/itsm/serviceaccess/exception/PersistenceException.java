package de.witcom.itsm.serviceaccess.exception;

public class PersistenceException extends BaseException {

    private static final long serialVersionUID = 38816005992204401L;

    public PersistenceException(String message, ErrorCode code) {
        super(message, code);
    }
}