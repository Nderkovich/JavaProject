package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.api.request.DocUpdateRequest;
import com.derkovich.springdocuments.exceptions.DocumentDoesntExistException;
import com.derkovich.springdocuments.exceptions.FileUploadException;
import com.derkovich.springdocuments.service.DocumentService;
import com.derkovich.springdocuments.service.dto.Document;
import com.derkovich.springdocuments.service.utils.FileServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/")
@CrossOrigin
public class AdminRESTController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private FileServer fileServer;

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(@RequestPart("document")MultipartFile doc, @RequestPart("desc") String desc) throws FileUploadException {
        Document document = new Document(doc.getOriginalFilename(), desc);

        if (fileServer.fileUpload(doc) && documentService.saveDocument(document)){
            return new ResponseEntity<>(document, HttpStatus.OK);
        } else {
            fileServer.deleteFile(doc.getOriginalFilename());
            throw new FileUploadException();
        }
    }

    @PutMapping("/document/{id:\\d+}")
    public ResponseEntity<Document> updateDocument(@PathVariable(value = "id") int id, @RequestBody DocUpdateRequest docUpdate) throws DocumentDoesntExistException {
        Document document = documentService.findById(id);
        if (document == null){
            throw new DocumentDoesntExistException();
        } else {
            document.setDescription(docUpdate.getDescription());
            documentService.saveDocument(document);
            return new ResponseEntity<>(document, HttpStatus.OK);
        }
    }

    @DeleteMapping("/document/{id:\\d+}")
    public ResponseEntity<Document> updateDocument(@PathVariable(value = "id") int id) throws DocumentDoesntExistException {
        Document document = documentService.findById(id);
        if (document == null){
            throw new DocumentDoesntExistException();
        } else {
            documentService.deleteById(id);
            fileServer.deleteFile(document.getName());
            return new ResponseEntity<>(document, HttpStatus.OK);
        }
    }
}
