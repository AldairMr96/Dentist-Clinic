package com.mycompany.clinica_odontologica.repository;

import com.mycompany.clinica_odontologica.model.Dentist;
import com.mycompany.clinica_odontologica.model.Turn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDentistRepository extends JpaRepository<Dentist, Long> {

}
