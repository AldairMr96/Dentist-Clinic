package com.mycompany.clinica_odontologica.controller.test;

import com.mycompany.clinica_odontologica.controller.PatientController;
import com.mycompany.clinica_odontologica.model.*;
import com.mycompany.clinica_odontologica.service.IPatientService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;
import static org.springframework.http.HttpStatus.*;

public class PatientControllerTest {

    @Mock
    private IPatientService patientService;

    @InjectMocks
    private PatientController patientController;

    @BeforeEach
    void setUp (){
        openMocks(this);
    }

    @Test
    void getPatientsSuccessTest (){
        List<Patient> patients = List.of(new Patient());

        when(patientService.getPatient()).thenReturn(patients);

        List<Patient> result = patientController.getPatient();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
    }

    @Test
    void getPatientEmptyTest (){
        List<Patient> patients = new ArrayList<>();

        when(patientService.getPatient()).thenReturn(patients);

        List<Patient> result = patientController.getPatient();

        assertNotNull(result);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    void findByIdPatientSuccessTest (){
        Long idPatient = 1L;
        MedicalInsuranceTypeEnum medicalInsuranceTypeEnum  =MedicalInsuranceTypeEnum.FREE;
        Patient patient = new Patient(medicalInsuranceTypeEnum, "O+", List.of(new Turn()), List.of(new Responsible()), new UserEntity());

        when(patientService.findPatientById(idPatient)).thenReturn(patient);

        ResponseEntity<?> response = patientController.findPatientById(idPatient);

        assertEquals(OK, response.getStatusCode());
        assertEquals(patient, response.getBody());
        verify(patientService, times(1)).findPatientById(idPatient);
    }
    @Test
    void findbyIdPatientNotfoundTest(){
        Long idPatient = 1L;

        when(patientService.findPatientById(idPatient)).thenThrow(new EntityNotFoundException("Patient not found"));

        ResponseEntity<?> response = patientController.findPatientById(idPatient);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Patient not found", response.getBody());
        verify(patientService, times(1)).findPatientById(idPatient);

    }

    @Test
    void findbyIdPatientInternalErrorTest(){
        Long idPatient = 1L;

        when(patientService.findPatientById(idPatient)).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity<?> response = patientController.findPatientById(idPatient);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(patientService, times(1)).findPatientById(idPatient);

    }

    @Test
    void createPatientSuccessTest (){
        MedicalInsuranceTypeEnum medicalInsuranceTypeEnum  =MedicalInsuranceTypeEnum.FREE;
        Patient patient = new Patient(medicalInsuranceTypeEnum, "O+", List.of(new Turn()), List.of(new Responsible()), new UserEntity());

        when(patientService.createPatient(patient)).thenReturn(patient);

        Patient result = patientController.createPatient(patient);

        assertEquals(patient.getMedicalInsuranceType(), result.getMedicalInsuranceType());
        assertEquals(patient.getBloodType(), result.getBloodType());
        assertFalse(result.getTurnsPatient().isEmpty());
        assertFalse(result.getResponsibles().isEmpty());

   }

}
