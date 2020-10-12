package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.service.DocumentService;
import com.derkovich.springdocuments.service.dto.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentsRESTController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("")
    public ResponseEntity<List<Document>> getDocuments(){
        return ResponseEntity.ok(documentService.allDocuments());
    }

    @GetMapping("{id:\\d+}")
    public ResponseEntity<Document> getDocument(@PathVariable(value = "id") int id){
        return ResponseEntity.ok(documentService.findById(id));
    }
}
