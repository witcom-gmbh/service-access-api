package de.witcom.itsm.serviceaccess.exception;

public enum ErrorCode {

    OBJECT_NOT_FOUND(404),
    SERVER_ERROR(500),
    BAD_REQUEST(400)
    ;

    private final int code;

    private ErrorCode(int code){
        this.code = code;
    }

    public int getValue(){
        return code;
    }
}