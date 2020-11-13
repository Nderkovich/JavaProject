package com.derkovich.springdocuments.exceptions;

public class TokenExpiredException extends Exception{
    public TokenExpiredException(){
        super("Token has lready expired");
    }
}
