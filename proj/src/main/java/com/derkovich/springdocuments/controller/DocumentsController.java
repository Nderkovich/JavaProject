package com.derkovich.springdocuments.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocumentsController {
    @GetMapping("/documents")
    public String documents(){return "documents";}
}
