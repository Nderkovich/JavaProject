package com.derkovich.springdocuments.repository;

import com.derkovich.springdocuments.service.dto.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Integer> {
    Document findFirstByName(String name);
    Document findFirstById(Integer id);
    void deleteById(Integer id);
}
