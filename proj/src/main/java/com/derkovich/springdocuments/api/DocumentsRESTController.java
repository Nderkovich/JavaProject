package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.api.request.SearchRequest;
import com.derkovich.springdocuments.service.DocumentService;
import com.derkovich.springdocuments.service.dto.Document;
import com.derkovich.springdocuments.service.utils.FileServer;
import com.derkovich.springdocuments.service.utils.JsonSerializers.DocumentView;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @GetMapping("")
    @JsonView(DocumentView.Simple.class)
    public ResponseEntity<List<Document>> getDocuments(){
        return ResponseEntity.ok(documentService.allDocuments());
    }

    @GetMapping("/{id:\\d+}")
    @JsonView(DocumentView.Detailed.class)
    public ResponseEntity<Document> getDocument(@PathVariable(value = "id") int id){
        return ResponseEntity.ok(documentService.findById(id));
    }

    @GetMapping("/search")
    @JsonView(DocumentView.Simple.class)
    public ResponseEntity<List<Document>> searchDocuments(@RequestBody SearchRequest request){
        return ResponseEntity.ok(documentService.findAllByDescriptionContaining(request.getSearch()));
    }

  /*  @GetMapping("/{id:\\d+}/download")
    public void getFile(@PathVariable(value = "id") int id, HttpServletResponse response) {
        Document document = documentService.findById(id);
        InputStream in = null;
        response.setHeader("Content-Disposition", "attachment; filename="+document.getName());
        try{
            in = new FileInputStream(new File(fileServer.getFilePath(document.getName())));
            int c;
            while ((c = in.read()) != -1){
                response.getWriter().write(c);
            }
        }
        catch (FileNotFoundException ex){
            //TODO
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                    response.getWriter().close();
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
*/

    @GetMapping("/{id:\\d+}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable(value = "id") int id,
                                                 HttpServletRequest request) {
        String fileName = documentService.findById(id).getName();
        Resource resource = null;
        if(fileName !=null && !fileName.isEmpty()) {
            try {
                resource = fileServer.loadFileAsResource(fileName);
            } catch (Exception e) {
                e.printStackTrace();
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
            return ResponseEntity.notFound().build();
        }
    }

}
