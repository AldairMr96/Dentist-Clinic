package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.Patient;
import com.mycompany.clinica_odontologica.model.Secretariat;
import com.mycompany.clinica_odontologica.model.Turn;

import java.time.LocalDate;
import java.util.List;

public interface ISecretariatService {
    public  Secretariat createSecretariat(Secretariat secretariat);

    public List<Secretariat> getSecretariats();

    public Secretariat findSecretariatById (Long idSecretariat);

    public void deleteSecretariatById (Long idSecretariat);

    public Secretariat editSecretariat(Secretariat secretariat);

    public List<Patient> getPatientsPerDay (LocalDate date);
}
