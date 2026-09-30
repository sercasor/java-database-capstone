package com.project.back_end.repo;

import com.project.back_end.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    //standard methods are implemented by Springboot

    /*-------------------------CUSTOM METHODS-------------------------*/
    //source: https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    public Admin findByUsername(String username);
}
