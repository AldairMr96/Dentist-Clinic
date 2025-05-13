package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.Dentist;

import java.util.List;

public interface IDentistService {
    public  Dentist createDentist (Dentist dentist);

    public List<Dentist> getDentists();

    public Dentist findDentistById (Long idDentist);

    public void deleteDentistById (Long idDentist);

    public Dentist editDentist (Dentist dentist);


}
