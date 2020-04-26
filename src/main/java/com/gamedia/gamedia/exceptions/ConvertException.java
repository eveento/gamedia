package com.gamedia.gamedia.exceptions;

public class ConvertException  extends RuntimeException{
    public ConvertException(String msg, Throwable e){
        super(msg, e);
    }
}
