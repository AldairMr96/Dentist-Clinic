package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.Dentist;
import com.mycompany.clinica_odontologica.repository.IDentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DentistService implements IDentistService{

    @Autowired
    private IDentistRepository dentistRepository;
    @Override
    public void createDentist(Dentist dentist) {
       dentistRepository.save(dentist);
    }

    @Override
    public List<Dentist> getDentists() {
        List<Dentist> dentists = dentistRepository.findAll();
        return dentists;
    }

    @Override
    public Dentist findDentistById(Long idDentist) {
        Dentist result = dentistRepository.findById(idDentist).orElseThrow(()->new RuntimeException("Dentist not Found"));
        return result;
    }

    @Override
    public void deleteDentistById(Long idDentist) {
        if(!dentistRepository.existsById(idDentist)) {
            throw new RuntimeException("Dentis not found");
        }
        dentistRepository.deleteById(idDentist);
    }

    @Override
    public Dentist editDentist(Dentist dentist) {
        //check if the dentist exists
        Dentist existingDentist = this.findDentistById(dentist.getIdPerson());
        //update data
        existingDentist.setDni(dentist.getDni());
        existingDentist.setName(dentist.getName());
        existingDentist.setLastname(dentist.getLastname());
        existingDentist.setNumberPhone(dentist.getNumberPhone());
        existingDentist.setAddress(dentist.getAddress());
        existingDentist.setSpeciality(dentist.getSpeciality());
        existingDentist.setDateOfBirth(dentist.getDateOfBirth());
        existingDentist.setDentistUser(dentist.getDentistUser());
        existingDentist.setScheduleDentist(dentist.getScheduleDentist());
        existingDentist.setTurnsDentist(dentist.getTurnsDentist());
        //Save new data for the dentist
        dentistRepository.save(existingDentist);

        return existingDentist;
    }
}
