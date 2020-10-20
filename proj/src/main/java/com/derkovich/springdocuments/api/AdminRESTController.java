package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.api.request.DocUpdateRequest;
import com.derkovich.springdocuments.repository.DocumentRepository;
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
    public ResponseEntity<Document> uploadDocument(@RequestParam("document")MultipartFile doc, @RequestParam("desc") String desc){
        Document document = new Document(doc.getOriginalFilename(), desc);

        if (fileServer.fileUpload(doc) && documentService.saveDocument(document)){
            return new ResponseEntity<>(document, HttpStatus.OK);
        } else {
            fileServer.deleteFile(doc.getOriginalFilename());
            throw new
        }
    }

    @PutMapping("/document/{id:\\d+}")
    public ResponseEntity updateDocument(@PathVariable(value = "id") int id, @RequestBody DocUpdateRequest docUpdate){
        Document document = documentService.findById(id);
        if (document == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("No such document");
        } else {
            document.setDescription(docUpdate.getDescription());
            documentService.saveDocument(document);
            return ResponseEntity.ok(document);
        }
    }

    @DeleteMapping("/document/{id:\\d+}")
    public ResponseEntity updateDocument(@PathVariable(value = "id") int id) {
        Document document = documentService.findById(id);
        if (document == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No such document");
        } else {
            documentService.deleteById(id);
            fileServer.deleteFile(document.getName());
            return ResponseEntity.ok(document);
        }
    }
}
