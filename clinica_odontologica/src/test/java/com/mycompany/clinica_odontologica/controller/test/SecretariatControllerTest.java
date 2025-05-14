package com.mycompany.clinica_odontologica.controller.test;

import com.mycompany.clinica_odontologica.controller.SecretariatController;
import com.mycompany.clinica_odontologica.model.*;
import com.mycompany.clinica_odontologica.service.ISecretariatService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;
import static org.springframework.http.HttpStatus.*;

public class SecretariatControllerTest {

    @Mock
    private ISecretariatService secretariatService;

    @InjectMocks
    private SecretariatController secretariatController;

    @BeforeEach
    void setUp (){
        openMocks(this);
    }

    @Test
    void getSecretariatTest (){
        Secretariat secretariat = new Secretariat();
        Secretariat secretariat1 = new Secretariat("Private", new UserEntity());
        List<Secretariat> secretariats = List.of(secretariat, secretariat1);

        when(secretariatService.getSecretariats()).thenReturn(secretariats);

        List<Secretariat> result = secretariatController.getSecretariats();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertFalse(result.isEmpty());
    }
    @Test
    void getSecretariatEmptyTest (){
        List<Secretariat> secretariats = new ArrayList<>();

        when(secretariatService.getSecretariats()).thenReturn(secretariats);

        List<Secretariat> result = secretariatController.getSecretariats();

        assertNotNull(result);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    void findSecretariatByIdSuccessTest (){

        Long idSecretariat =1L;
        Secretariat secretariat = new Secretariat("Private", new UserEntity());

        when(secretariatService.findSecretariatById(idSecretariat)).thenReturn(secretariat);

        ResponseEntity<?> response = secretariatController.findSecretariatById(idSecretariat);

        assertEquals(OK, response.getStatusCode());
        assertEquals(secretariat, response.getBody());
        verify(secretariatService, times(1)).findSecretariatById(idSecretariat);

    }

    @Test
    void findSecretariatByIdNotFoundTest (){
        Long idSecretariat =1L;

        when(secretariatService.findSecretariatById(idSecretariat)).thenThrow(new EntityNotFoundException("Secretariat not found"));

        ResponseEntity<?> response = secretariatController.findSecretariatById(idSecretariat);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Secretariat not found", response.getBody());
        verify(secretariatService, times(1)).findSecretariatById(idSecretariat);
    }
    @Test
    void findSecretariatByIdInternalServerTest (){
        Long idSecretariat =1L;

        when(secretariatService.findSecretariatById(idSecretariat)).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity<?> response = secretariatController.findSecretariatById(idSecretariat);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(secretariatService, times(1)).findSecretariatById(idSecretariat);
    }

    @Test
    void createSecretariatTest (){
        Secretariat secretariat = new Secretariat("Private", new UserEntity());
        when(secretariatService.createSecretariat(any(Secretariat.class))).thenReturn(secretariat);

        Secretariat result = secretariatController.createSecretariat(secretariat);

        assertEquals(secretariat.getSector(), result.getSector());
        assertEquals(secretariat.getSecretariatUserEntity(), result.getSecretariatUserEntity());
        verify(secretariatService, times(1)).createSecretariat(secretariat);
    }

    @Test
    void editSecretariatSuccesTest (){

        Secretariat secretariat = new Secretariat("Private", new UserEntity());

        when(secretariatService.editSecretariat(any(Secretariat.class))).thenReturn(secretariat);

        ResponseEntity<?> response = secretariatController.editSecretariat(secretariat);

        assertEquals(OK, response.getStatusCode());
        assertEquals(secretariat, response.getBody());
        verify(secretariatService, times(1)).editSecretariat(secretariat);
    }

    @Test
    void editSecretariatNotFoundTest(){
        Secretariat secretariat = new Secretariat();

        when(secretariatService.editSecretariat(any(Secretariat.class))).thenThrow(new EntityNotFoundException("Secretariat not found"));

        ResponseEntity<?> response = secretariatController.editSecretariat(secretariat);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Secretariat not found", response.getBody());
        verify(secretariatService, times(1)).editSecretariat(secretariat);
    }

    @Test
    void editSecretariatInternalErrorTest(){
        Secretariat secretariat = new Secretariat();

        when(secretariatService.editSecretariat(any(Secretariat.class))).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity<?> response = secretariatController.editSecretariat(secretariat);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(secretariatService, times(1)).editSecretariat(secretariat);
    }

    @Test
    void deleteSecretariatSuccessTest (){
        Long idSecretariat = 1L;

        doNothing().when(secretariatService).deleteSecretariatById(idSecretariat);

        ResponseEntity<?> response = secretariatController.deleteSecretariat(idSecretariat);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Delete Secretariat susccessfully", response.getBody());
        verify(secretariatService, times(1)).deleteSecretariatById(idSecretariat);
    }

    @Test
    void deleteSecretariatNotFoundTest (){
        Long idSecretariat = 1L;

        doThrow(new EntityNotFoundException("Secretariat not found")).when(secretariatService).deleteSecretariatById(idSecretariat);

        ResponseEntity<?> response = secretariatController.deleteSecretariat(idSecretariat);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Secretariat not found", response.getBody());
        verify(secretariatService, times(1)).deleteSecretariatById(idSecretariat);
    }
    @Test
    void deleteSecretariatInternalErrorTest (){
        Long idSecretariat = 1L;

        doThrow(new RuntimeException("Server internal Error")).when(secretariatService).deleteSecretariatById(idSecretariat);

        ResponseEntity<?> response = secretariatController.deleteSecretariat(idSecretariat);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(secretariatService, times(1)).deleteSecretariatById(idSecretariat);
    }

    @Test
    void getPatientsPerDayEmptyTest (){
        List<Patient> patients = new ArrayList<>();
        LocalDate localDate = LocalDate.of(2025,05, 8);

        when(secretariatService.getPatientsPerDay(localDate)).thenReturn(patients);

        ResponseEntity<?> result = secretariatController.getPatientsPerDay(localDate);

        assertNotNull(result);
        assertEquals(OK, result.getStatusCode());
        assertEquals(patients, result.getBody());
        verify(secretariatService, times(1)).getPatientsPerDay(localDate);
    }
    @Test
    void getPatientsPerDayTest (){
        MedicalInsuranceTypeEnum medicalInsuranceTypeEnum  =MedicalInsuranceTypeEnum.FREE;
        Patient patient = new Patient(medicalInsuranceTypeEnum, "O+", List.of(new Turn()), List.of(new Responsible()), new UserEntity());
        Patient patient1 = new Patient(medicalInsuranceTypeEnum, "AB-",List.of(new Turn()), List.of(new Responsible()), new UserEntity() );
        List<Patient> patients = List.of(patient1, patient);
        LocalDate localDate = LocalDate.of(2025,05, 8);

        when(secretariatService.getPatientsPerDay(localDate)).thenReturn(patients);

        ResponseEntity<?> result = secretariatController.getPatientsPerDay(localDate);

        assertNotNull(result);
        assertEquals(OK, result.getStatusCode());
        assertEquals(patients, result.getBody());
        verify(secretariatService, times(1)).getPatientsPerDay(localDate);
    }

    @Test
    void getPatientsPerDayInternalErrorTest(){

        LocalDate localDate = LocalDate.of(2025,05, 8);

        when(secretariatService.getPatientsPerDay(localDate)).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity<?> result = secretariatController.getPatientsPerDay(localDate);

        assertNotNull(result);
        assertEquals(INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Server internal Error", result.getBody());
        verify(secretariatService, times(1)).getPatientsPerDay(localDate);
    }
}
