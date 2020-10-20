package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.api.request.SearchRequest;
import com.derkovich.springdocuments.exceptions.DocumentDoesntExistException;
import com.derkovich.springdocuments.exceptions.FileLoadException;
import com.derkovich.springdocuments.service.DocumentService;
import com.derkovich.springdocuments.service.dto.Document;
import com.derkovich.springdocuments.service.utils.FileServer;
import com.derkovich.springdocuments.service.utils.JsonSerializers.DocumentView;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin
public class DocumentsRESTController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private FileServer fileServer;


    @Operation(summary = "Get list of documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of documents",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Document.class)) })
    })
    @GetMapping("")
    @JsonView(DocumentView.Simple.class)
    public ResponseEntity<List<Document>> getDocuments(){
        return new ResponseEntity<>(documentService.allDocuments(), HttpStatus.OK);
    }

    @Operation(summary = "Get specific document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Document.class)) }),
            @ApiResponse(responseCode = "404", description = "Invalid id supplied",
                            content = @Content)
    })
    @GetMapping("/{id:\\d+}")
    @JsonView(DocumentView.Detailed.class)
    public ResponseEntity<Document> getDocument(@Parameter(description = "Id of document to get") @PathVariable(value = "id") int id) throws DocumentDoesntExistException {
        Document document = documentService.findById(id);
        if (document != null) {
            return new ResponseEntity<>(documentService.findById(id), HttpStatus.OK);
        } else {
            throw new DocumentDoesntExistException();
        }
    }

    @Operation(summary = "Search documents by description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of documents",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Document.class)) }),
    })
    @GetMapping("/search")
    @JsonView(DocumentView.Simple.class)
    public ResponseEntity<List<Document>> searchDocuments(@Parameter(description = "Search parameters to look for") @RequestBody SearchRequest request){
        return new ResponseEntity<>(documentService.findAllByDescriptionContaining(request.getSearch()), HttpStatus.OK);
    }

    @GetMapping("/{id:\\d+}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable(value = "id") int id,
                                                 HttpServletRequest request) throws DocumentDoesntExistException, FileLoadException {
        Document document = documentService.findById(id);
        if (document == null){
            throw new DocumentDoesntExistException();
        }
        String fileName = document.getName();
        Resource resource = null;
        if(fileName !=null && !fileName.isEmpty()) {
            try {
                resource = fileServer.loadFileAsResource(fileName);
            } catch (Exception e) {
                throw new FileLoadException();
            }
            // Try to determine file's content type
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                //logger.info("Could not determine file type.");
            }
            // Fallback to the default content type if type could not be determined
            if(contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            throw new DocumentDoesntExistException();
        }
    }

}
