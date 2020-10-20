package com.derkovich.springdocuments.exceptions;

public class FileUploadException extends Exception {
    public FileUploadException(){
        super("Error uploading file");
    }
}
