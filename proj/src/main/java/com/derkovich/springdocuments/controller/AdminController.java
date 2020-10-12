package com.derkovich.springdocuments.controller;

import com.derkovich.springdocuments.forms.UploadDocumentForm;
import com.derkovich.springdocuments.security.UserDetailsServiceImpl;
import com.derkovich.springdocuments.service.DocumentService;
import com.derkovich.springdocuments.service.dto.Document;
import com.derkovich.springdocuments.service.utils.FileServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private FileServer fileServer;

    @GetMapping("/admin/upload")
    public String uploadFile(Model model){
        UploadDocumentForm uploadDocumentForm = new UploadDocumentForm();
        model.addAttribute("uploadDocumentForm", uploadDocumentForm);
        return "fileUploadView";
    }

    @PostMapping("/admin/upload")
    public String submit(Model model, @ModelAttribute("uploadDocumentForm") UploadDocumentForm documentForm) {
        Document document = new Document(documentForm.getFile().getOriginalFilename(), documentForm.getDescription());

        if (documentService.saveDocument(document)){
            fileServer.fileUpload(documentForm.getFile());
            return "redirect:/";
        }
        return "fileUploadView";
    }
}
