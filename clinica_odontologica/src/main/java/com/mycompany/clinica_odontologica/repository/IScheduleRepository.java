package com.mycompany.clinica_odontologica.repository;

import com.mycompany.clinica_odontologica.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IScheduleRepository extends JpaRepository< Schedule,Long> {
}
