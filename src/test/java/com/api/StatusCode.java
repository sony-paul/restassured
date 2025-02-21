package com.api;

public enum StatusCode {

    CODE_200(200,"Success"),
    CODE_201(201,"CREATED"),
    CODE_401(401,"UNAUTHORISED");

    private final int code;
    private final String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    StatusCode(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }
}
