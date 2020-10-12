package com.derkovich.springdocuments.service;

import com.derkovich.springdocuments.repository.DocumentRepository;
import com.derkovich.springdocuments.service.dto.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public List<Document> allDocuments(){
        return (List<Document>) documentRepository.findAll();
    }

    public Document findById (Integer id){
        return documentRepository.findFirstById(id);
    }

    public boolean saveDocument(Document document){
        Document documentFromDB = documentRepository.findFirstByName(document.getName());
        if (documentFromDB != null){
            return false;
        }
        documentRepository.save(document);
        return true;
    }

    public Document findFirstByName(String name){
        return documentRepository.findFirstByName(name);
    }

    public void deleteById(Integer id){
        documentRepository.deleteById(id);
    }
}
