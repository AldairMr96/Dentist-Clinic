package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.MedicalInsuranceTypeEnum;
import com.mycompany.clinica_odontologica.model.Patient;
import com.mycompany.clinica_odontologica.repository.IPatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        patientFound.setMedicalInsuranceType(patient.getMedicalInsuranceType());
        patientFound.setBloodType(patient.getBloodType());
        patientFound.setTurnsPatient(patient.getTurnsPatient());
        patientFound.setResonsibles(patient.getResonsibles());
        this.createPatient(patientFound);
        return patientFound;
    }

    @Override
    public List<Patient> getPatientWithMedicalInsure(String medicalInsuranceType) {
        List<Patient> allPatients= this.getPatient();
        List<Patient> patientsWhitInsuranceType = new ArrayList<>();
        MedicalInsuranceTypeEnum enumMedicalInsurance = MedicalInsuranceTypeEnum.valueOf(medicalInsuranceType);
        if (!allPatients.isEmpty() && allPatients != null) {
            for (Patient patient : allPatients) {
                if ( patient.getMedicalInsuranceType() == enumMedicalInsurance ) {
                    patientsWhitInsuranceType.add(patient);
                }
            }

        }
        return patientsWhitInsuranceType;
    }
}
