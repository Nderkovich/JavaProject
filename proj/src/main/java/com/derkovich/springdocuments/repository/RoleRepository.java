package com.derkovich.springdocuments.repository;

import com.derkovich.springdocuments.service.dto.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findFirstById(Integer id);
}
