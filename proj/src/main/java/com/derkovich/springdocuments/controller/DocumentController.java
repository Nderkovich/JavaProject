package com.derkovich.springdocuments.controller;

import com.derkovich.springdocuments.forms.AddCommentForm;
import com.derkovich.springdocuments.security.MyUserDetails;
import com.derkovich.springdocuments.security.UserDetailsServiceImpl;
import com.derkovich.springdocuments.service.CommentService;
import com.derkovich.springdocuments.service.DocumentService;
import com.derkovich.springdocuments.service.dto.Comment;
import com.derkovich.springdocuments.service.dto.Document;
import com.derkovich.springdocuments.service.utils.FileServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FileServer fileServer;

    @GetMapping("/documents")
    public String getDocuments(Model model){
        List<Document> documents = documentService.allDocuments();
        model.addAttribute("documents", documents);
        return "documentsList";
    }

    @GetMapping("/documents/{id:\\d+}")
    public String getDocument(@PathVariable(value = "id") int id, Model model){
        Document document = documentService.findById(id);
        List<Comment> comments= commentService.findAllByDocumentId(id);
        AddCommentForm commentForm = new AddCommentForm();
        if (document != null){
            model.addAttribute("document", document);
            model.addAttribute("comments", comments);
            model.addAttribute("commentForm", commentForm);
            return "documentView";
        }
        else {
            // TODO
            return "redirect:/";
        }
    }

    // TODO
    @GetMapping("/documents/{id:\\d+}/download")
    public String getFile(@PathVariable(value = "id") int id, HttpServletResponse response) {
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
        return "redirect:/document/" + id;
    }

    @PostMapping("/documents/{id:\\d+}/comments")
    public String addComment (@PathVariable(value = "id") int id, AddCommentForm commentForm, Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = "";
        if (principal instanceof MyUserDetails){
            System.out.println("f");
            userName = ((MyUserDetails)principal).getUsername();
        }
        Comment comment = new Comment(commentForm.getComment(),
                                        documentService.findById(id),
                                        userDetailsService.getUserByName(userName));
        if (commentService.saveComment(comment)){
            return "redirect:/document/" + id;
        } else {
            // TODO
            return "redirect:/";
        }
    }
}