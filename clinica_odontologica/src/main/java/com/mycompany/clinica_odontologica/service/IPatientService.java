package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.Patient;

import java.util.List;

public interface IPatientService {
    public  Patient createPatient (Patient patient);

    public List<Patient> getPatient();

    public Patient findPatientById (Long idPatient);

    public void deletePatientById (Long idPatient);

    public Patient editPatient (Patient patient);

    public List<Patient> getPatientWithMedicalInsure(String medicalInsuranceType);

}

