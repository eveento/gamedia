package com.gamedia.gamedia.exceptions;

public class CreateDtoException extends RuntimeException {
    public CreateDtoException(String msg, Throwable e){
        super(msg, e);
    }
}
