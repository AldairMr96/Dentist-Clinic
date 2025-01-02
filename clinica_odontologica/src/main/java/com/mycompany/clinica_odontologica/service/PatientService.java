package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.Patient;
import com.mycompany.clinica_odontologica.repository.IPatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PatientService implements IPatientService{

    @Autowired
    private IPatientRepository patientRepository;
    @Override
    public void createPatient(Patient patient) {
        patientRepository.save(patient);
    }

    @Override
    public List<Patient> getPatient() {
        List<Patient> patients = patientRepository.findAll();
        return patients;
    }

    @Override
    public Patient findPatientById(Long idPatient) {
        Patient patient = patientRepository.findById(idPatient).
                orElseThrow(()-> new EntityNotFoundException("Patient not found"));
        return patient;
    }

    @Override
    public void deletePatientById(Long idPatient) {
        if(!patientRepository.existsById(idPatient)) {
            throw new EntityNotFoundException("Patient not found");
        }
        patientRepository.deleteById(idPatient);
    }

    @Override
    public Patient editPatient(Patient patient) {
        Patient patientFound = this.findPatientById(patient.getIdPerson());

        patientFound.setDni(patient.getDni());
        patientFound.setName(patient.getName());
        patientFound.setLastname(patient.getLastname());
        patientFound.setNumberPhone(patient.getNumberPhone());
        patientFound.setAddress(patient.getAddress());
        patientFound.setDateOfBirth(patient.getDateOfBirth());
        patientFound.setMedicalIsure(patient.getMedicalIsure());
        patientFound.setBloodType(patient.getBloodType());
        patientFound.setTurnsPatient(patient.getTurnsPatient());
        patientFound.setResonsibles(patient.getResonsibles());
        this.createPatient(patientFound);
        return patientFound;
    }
}
