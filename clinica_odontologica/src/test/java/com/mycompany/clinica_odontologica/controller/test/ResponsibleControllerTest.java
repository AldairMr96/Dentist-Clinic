package com.mycompany.clinica_odontologica.controller.test;

import com.mycompany.clinica_odontologica.controller.ResponsibleController;
import com.mycompany.clinica_odontologica.model.Patient;
import com.mycompany.clinica_odontologica.model.Responsible;
import com.mycompany.clinica_odontologica.service.IResponsibleService;
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
public class ResponsibleControllerTest {

    @Mock
    private IResponsibleService responsibleService;
    @InjectMocks
    private ResponsibleController responsibleController;

    @BeforeEach
    void setUp (){
        openMocks(this);
    }

    @Test
    void getResponsiblesTest (){
        Patient patient = new Patient();
        Responsible responsible = new Responsible("Father", patient);
        List<Responsible>  responsibles = List.of(responsible);
        when(responsibleService.getResponsibles()).thenReturn(responsibles);

        List<Responsible> result = responsibleController.getResponsible();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        verify(responsibleService, times(1)).getResponsibles();

    }

    @Test
    void getResponsiblesEmptyTest (){
        List<Responsible>  responsibles = new ArrayList<>();
        when(responsibleService.getResponsibles()).thenReturn(responsibles);

        List<Responsible> result = responsibleController.getResponsible();

        assertNotNull(result);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        verify(responsibleService, times(1)).getResponsibles();
    }
    @Test
    void findResponsibleByIdSuccessTest (){
        Long idResponsible = 1L;
        Patient patient = new Patient();
        Responsible responsible = new Responsible("Father", patient);

        when(responsibleService.finResponsibletById(idResponsible)).thenReturn(responsible);

        ResponseEntity<?> response = responsibleController.findResponsibleById(idResponsible);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(responsible, response.getBody());
        verify(responsibleService, times(1)).finResponsibletById(idResponsible);
    }

    @Test
    void findResponsibleByIdNotFoundTest (){

        Long idResponsible =1L;

        when(responsibleService.finResponsibletById(idResponsible)).thenThrow(new EntityNotFoundException("Responsible not found"));

        ResponseEntity<?> response = responsibleController.findResponsibleById(idResponsible);
        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Responsible not found", response.getBody());
        verify(responsibleService, times(1)).finResponsibletById(idResponsible);
    }
    @Test
    void findResponsibleByIdInternalErrorTest (){

        Long idResponsible =1L;

        when(responsibleService.finResponsibletById(idResponsible)).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity<?> response = responsibleController.findResponsibleById(idResponsible);
        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(responsibleService, times(1)).finResponsibletById(idResponsible);
    }

    @Test
    void createResponsibleSuccessTest(){

        Patient patient = new Patient();
        Responsible responsible = new Responsible("Father", patient);

        when(responsibleService.createResponsible(any(Responsible.class))).thenReturn(responsible);

        Responsible result = responsibleController.createResponsible(responsible);

        assertNotNull(result);
        assertEquals(responsible.getRelationshipType(), result.getRelationshipType());
        assertEquals(responsible.getRelationshipPatient(), result.getRelationshipPatient());
        verify(responsibleService, times(1)).createResponsible(responsible);
    }

    @Test
    void editResponsibleSuccessTest (){
        Patient patient = new Patient();
        Responsible responsible = new Responsible("Father", patient);

        when(responsibleService.editResponsible(any(Responsible.class))).thenReturn(responsible);

        ResponseEntity<?> response = responsibleController.editResponsible(responsible);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(responsible, response.getBody());
        verify(responsibleService, times(1)).editResponsible(responsible);
    }

    @Test
    void editResponsibleNotFoundTest (){
        Patient patient = new Patient();
        Responsible responsible = new Responsible("Father", patient);


        when(responsibleService.editResponsible(responsible)).thenThrow(new EntityNotFoundException("Responsible not found"));

        ResponseEntity<?> response = responsibleController.editResponsible(responsible);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Responsible not found", response.getBody());
        verify(responsibleService, times(1)).editResponsible(any(Responsible.class));
    }
    @Test
    void editResponsibleInternalErrorTest (){
        Patient patient = new Patient();
        Responsible responsible = new Responsible("Father", patient);


        when(responsibleService.editResponsible(responsible)).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity<?> response = responsibleController.editResponsible(responsible);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(responsibleService, times(1)).editResponsible(any(Responsible.class));
    }

    @Test
    void deleteResponsibleSuccessTest (){
       Long idResponsible =1L;

       doNothing().when(responsibleService).deleteResponsibleById(idResponsible);

       ResponseEntity<?> response = responsibleController.deleteResponsible(idResponsible);

       assertNotNull(response);
       assertEquals(OK, response.getStatusCode());
       assertEquals("Delete responsible susccessfully", response.getBody());
       verify(responsibleService, times(1)).deleteResponsibleById(idResponsible);
    }

    @Test
    void deleteResponsibleNotFoundTest (){
        Long idResponsible =1L;

        doThrow(new EntityNotFoundException("Responsible not found")).when(responsibleService).deleteResponsibleById(idResponsible);

        ResponseEntity<?> response = responsibleController.deleteResponsible(idResponsible);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Responsible not found", response.getBody());
        verify(responsibleService, times(1)).deleteResponsibleById(idResponsible);
    }
    @Test
    void deleteResponsibleInternalErrorTest (){
        Long idResponsible =1L;

        doThrow(new RuntimeException("Server internal Error")).when(responsibleService).deleteResponsibleById(idResponsible);

        ResponseEntity<?> response = responsibleController.deleteResponsible(idResponsible);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(responsibleService, times(1)).deleteResponsibleById(idResponsible);
    }

}
