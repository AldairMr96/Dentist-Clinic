package com.mycompany.clinica_odontologica.repository;

import com.mycompany.clinica_odontologica.model.Turn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ITurnRepository extends JpaRepository<Turn,Long> {

    List<Turn> findByDentist_IdPersonAndDateTurn(Long dentistId, LocalDate dateTurn);
}
