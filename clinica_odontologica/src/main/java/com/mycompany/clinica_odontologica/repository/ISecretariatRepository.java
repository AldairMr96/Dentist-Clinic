package com.mycompany.clinica_odontologica.repository;

import com.mycompany.clinica_odontologica.model.Secretariat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISecretariatRepository extends JpaRepository< Secretariat,Long> {
}
