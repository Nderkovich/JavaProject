package com.derkovich.springdocuments.aoplog;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class AOPLogger {

    @Pointcut("execution(* com.derkovich.springdocuments.api.AdminRESTController.uploadDocument(..))")
    public void adminUpload(){}

    @Before("adminUpload()")
    public void beforeUpload(){
        log.trace("Admin upload start");
    }

    @After("adminUpload()")
    public void aftereUpload(){
        log.trace("Admin has uploaded file");
    }

    @Pointcut("execution(* com.derkovich.springdocuments.api.AdminRESTController.updateDocument(..))")
    public void adminUpdate(){}

    @Before("adminUpdate()")
    public void beforeUpdate(){
        log.trace("Admin stared document update");
    }

    @After("adminUpdate()")
    public void afterUpdate(){
        log.trace("Admin has updated document");
    }

    @Pointcut("execution(* com.derkovich.springdocuments.api.AdminRESTController.deleteDocument())")
    public void deleteDocument(){}


    @Before("deleteDocument()")
    public void beforeDelete(){
        log.trace("Admin stared document delete");
    }

    @After("deleteDocument()")
    public void afterDelete(){
        log.trace("Admin has deleted document");
    }

    @Before("execution(* com.derkovich.springdocuments.api.WebRestControllerAdvice.handleNotFoundException(..))")
    public void beforeNotFoun(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Exception ex = (Exception)args[1];
        log.trace("Not found exception occured" + ex.getMessage());
    }
}
