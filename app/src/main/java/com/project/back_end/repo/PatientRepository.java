package com.project.back_end.repo;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {

    //standard methods are implemented by Springboot

    /*-------------------------CUSTOM METHODS-------------------------*/
    public Patient  findByEmail(String email);
    public Patient  findByEmailOrPhone(
            String email,
            String phone);


}

