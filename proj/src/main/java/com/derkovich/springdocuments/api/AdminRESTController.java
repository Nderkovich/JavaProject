package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.service.DocumentService;
import com.derkovich.springdocuments.service.dto.Document;
import com.derkovich.springdocuments.service.utils.FileServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/")
public class AdminRESTController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private FileServer fileServer;

    //TODO if file already exists ??
    @PostMapping("/upload")
    public String uploadDocument(@RequestParam("document")MultipartFile doc, @RequestParam("desc") String desc){
        Document document = new Document(doc.getOriginalFilename(), desc);

        if (documentService.saveDocument(document) && fileServer.fileUpload(doc)){
            return "OK";
        } else {
            documentService.deleteById(documentService.findFirstByName(doc.getOriginalFilename()).getId());;
            return "NOT OK";
        }
    }
}
