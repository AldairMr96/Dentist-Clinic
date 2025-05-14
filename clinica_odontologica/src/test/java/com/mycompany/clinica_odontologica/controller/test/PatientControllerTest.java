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

    @Test
    void editPatientSuccessTest (){
        MedicalInsuranceTypeEnum medicalInsuranceTypeEnum  =MedicalInsuranceTypeEnum.FREE;
        Patient patient = new Patient(medicalInsuranceTypeEnum, "O+", List.of(new Turn()), List.of(new Responsible()), new UserEntity());

        when(patientService.editPatient(any(Patient.class))).thenReturn(patient);

        ResponseEntity<?> response = patientController.editPatient(patient);

        assertEquals(OK, response.getStatusCode());
        assertEquals(patient, response.getBody());
        verify(patientService, times(1)).editPatient(patient);
    }

    @Test
    void editPatientNotFoundTest (){

        Patient patient = new Patient();

        when(patientService.editPatient(any(Patient.class))).thenThrow(new EntityNotFoundException("Patient not found"));

        ResponseEntity<?> response = patientController.editPatient(patient);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Patient not found", response.getBody());
        verify(patientService, times(1)).editPatient(patient);
    }

    @Test
    void editPatientInternalErrorTest (){
        Patient patient = new Patient();

        when(patientService.editPatient(any(Patient.class))).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity<?> response = patientController.editPatient(patient);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(patientService, times(1)).editPatient(patient);
    }

    @Test
    void  deletePatientSuccessTest(){
        Long idPatient = 1L;
        doNothing().when(patientService).deletePatientById(idPatient);

        ResponseEntity<?> response = patientController.deletePatient(idPatient);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Delete patient susccessfully", response.getBody());
        verify(patientService, times(1)).deletePatientById(idPatient);
    }
    @Test
    void deletePatientNotFoundTest(){
        Long idPatient = 1L;
        doThrow(new EntityNotFoundException("Patient not found")).when(patientService).deletePatientById(idPatient);

        ResponseEntity<?> response = patientController.deletePatient(idPatient);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Patient not found", response.getBody());
        verify(patientService, times(1)).deletePatientById(idPatient);
    }

    @Test
    void deletePatientInternalErrorTest(){
        Long idPatient = 1L;
        doThrow(new RuntimeException("Server internal Error")).when(patientService).deletePatientById(idPatient);

        ResponseEntity<?> response = patientController.deletePatient(idPatient);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(patientService, times(1)).deletePatientById(idPatient);
    }

    @Test
    void  getMedicalInsureSuccessTest (){
        MedicalInsuranceTypeEnum medicalInsuranceTypeEnum  =MedicalInsuranceTypeEnum.FREE;
        Patient patient = new Patient(medicalInsuranceTypeEnum, "O+", List.of(new Turn()), List.of(new Responsible()), new UserEntity());
        List<Patient> patientList = List.of(patient);

        when(patientService.getPatientWithMedicalInsure(medicalInsuranceTypeEnum.name())).thenReturn(patientList);

        ResponseEntity<?> response = patientController.getPatientWithMedicalInsure(medicalInsuranceTypeEnum.name());

        assertEquals(OK, response.getStatusCode());
        assertEquals(patientList, response.getBody());
        verify(patientService, times(1)).getPatientWithMedicalInsure(medicalInsuranceTypeEnum.name());
    }

    @Test
    void  getMedicalInsureNotFoundTest (){
        MedicalInsuranceTypeEnum medicalInsuranceTypeEnum  =MedicalInsuranceTypeEnum.FREE;

        when(patientService.getPatientWithMedicalInsure(medicalInsuranceTypeEnum.name())).thenThrow(new EntityNotFoundException("Patient not found"));

        ResponseEntity<?> response = patientController.getPatientWithMedicalInsure(medicalInsuranceTypeEnum.name());

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Patient not found", response.getBody());
        verify(patientService, times(1)).getPatientWithMedicalInsure(medicalInsuranceTypeEnum.name());
    }
    @Test
    void  getMedicalInsureInternalErrorTest (){
        MedicalInsuranceTypeEnum medicalInsuranceTypeEnum  =MedicalInsuranceTypeEnum.FREE;

        when(patientService.getPatientWithMedicalInsure(medicalInsuranceTypeEnum.name())).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity<?> response = patientController.getPatientWithMedicalInsure(medicalInsuranceTypeEnum.name());

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(patientService, times(1)).getPatientWithMedicalInsure(medicalInsuranceTypeEnum.name());
    }
}
