package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.Dentist;
import com.mycompany.clinica_odontologica.model.Schedule;
import com.mycompany.clinica_odontologica.model.Turn;
import com.mycompany.clinica_odontologica.repository.IDentistRepository;
import com.mycompany.clinica_odontologica.repository.IScheduleRepository;
import com.mycompany.clinica_odontologica.repository.ITurnRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DentistService implements IDentistService{

    @Autowired
    private IDentistRepository dentistRepository;
    @Autowired
    private ITurnRepository turnRepository;
    @Autowired
    private IScheduleRepository scheduleRepository;
    @Override
    public Dentist createDentist(Dentist dentist) {
        if (dentist.getScheduleDentist() != null) {
            Schedule schedule = dentist.getScheduleDentist();

            schedule = scheduleRepository.findById(schedule.getIdSchedule())
                       .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));


            dentist.setScheduleDentist(schedule); // Add Schedule to Dentist
        }
       return dentistRepository.save(dentist);
    }

    @Override
    public List<Dentist> getDentists() {
        List<Dentist> dentists = dentistRepository.findAll();
        return dentists;
    }

    @Override
    public Dentist findDentistById(Long idDentist) {
        Dentist result = dentistRepository.findById(idDentist).
                orElseThrow(()->new EntityNotFoundException("Dentist not Found"));
        return result;
    }

    @Override
    public void deleteDentistById(Long idDentist) {
        if(!dentistRepository.existsById(idDentist)) {
            throw new EntityNotFoundException("Dentis not found");
        }
        dentistRepository.deleteById(idDentist);
    }

    @Override
    public Dentist editDentist(Dentist dentist) {
        //check if the dentist exists

        Dentist dentistFound = this.findDentistById(dentist.getIdPerson());
        //update data
        dentistFound.setDni(dentist.getDni());
        dentistFound.setName(dentist.getName());
        dentistFound.setLastname(dentist.getLastname());
        dentistFound.setNumberPhone(dentist.getNumberPhone());
        dentistFound.setAddress(dentist.getAddress());
        dentistFound.setDateOfBirth(dentist.getDateOfBirth());
        dentistFound.setSpeciality(dentist.getSpeciality());
        dentistFound.setDentistUserEntity(dentist.getDentistUserEntity());
        dentistFound.setScheduleDentist(dentist.getScheduleDentist());
        //Save new data for the dentist
        this.createDentist(dentistFound);

        return dentistFound;
    }
}
