package com.derkovich.springdocuments.repository;

import com.derkovich.springdocuments.service.dto.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User getUserByUsername(String username);
    Optional<User> findFirstByUsernameAndPassword(String username, String password);
}
