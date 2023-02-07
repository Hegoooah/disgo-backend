package com.hegoo.disgo.utils;

public enum ResultCode {
    SUCCESS(200, "Success!"),
    SUCCESS_WITH_TOKEN(201, "Success with token!"),
    UNAUTHORIZED(401, "User unauthorized!"),
    USER_NOT_FOUND(402, "User not found!"),
    CONNECTION_ERROR(403, "Server connection error!");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
