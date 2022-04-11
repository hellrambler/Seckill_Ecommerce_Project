package com.seckillproject.error;

public enum EnumBusinessError implements CommonError{
    // common error type - starts with 1000
    PARAMETER_VALIDATION_ERROR(10001, "Parameter is not valid"),
    // unknown error - 10002
    UNKNOWN_ERROR(10002, "Unknown error"),
    // starts with 2000 - user info related
    USER_NOT_EXIST(20001, "User not exists"),
    USER_LOGIN_FAIL(20002, "The mobile number or password provided is not correct"),
    USER_NOT_LOGIN(20003, "User needs to log in first"),
    // starts with 3000 - transactional error
    INSUFFICIENT_INVENTORY(30001, "Insufficient inventory amount")
    ;

    private EnumBusinessError(int errCode, String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
