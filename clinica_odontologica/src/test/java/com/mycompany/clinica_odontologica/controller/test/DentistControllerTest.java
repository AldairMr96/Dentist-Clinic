package com.mycompany.clinica_odontologica.controller.test;

import com.mycompany.clinica_odontologica.controller.DentistController;
import com.mycompany.clinica_odontologica.model.*;
import com.mycompany.clinica_odontologica.repository.IDentistRepository;
import com.mycompany.clinica_odontologica.service.IDentistService;
import com.mycompany.clinica_odontologica.service.TurnService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;
import static org.springframework.http.HttpStatus.*;

public class DentistControllerTest {
    @Mock
    private IDentistService dentistService;


    @InjectMocks
    private DentistController dentistController;

    @BeforeEach
    void setUp (){
        openMocks(this);
    }

    @Test
    void testGetDentistSuccess(){
        Dentist dentist = new Dentist();
        List<Dentist> dentists = List.of(dentist);

        when(dentistService.getDentists()).thenReturn(dentists);

         List<Dentist> result = dentistController.getDentists();

         assertNotNull(result);
         assertEquals(1, result.size());
         assertFalse(result.isEmpty());
    }
    @Test
    void testGetDentistEmpty(){

        List<Dentist> dentists =new ArrayList<>();

        when(dentistService.getDentists()).thenReturn(dentists);

        List<Dentist> result = dentistController.getDentists();

        assertNotNull(result);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateDentsit (){
        Turn turn = new Turn();
        Dentist dentist = new Dentist( "Endodoncist", new UserEntity(), new Schedule(),List.of(turn) );

        when(dentistService.createDentist(dentist)).thenReturn(dentist);

        Dentist result = dentistController.createDentist(dentist);

        assertEquals(dentist.getSpeciality(), result.getSpeciality());
        assertEquals(dentist.getDentistUserEntity(), result.getDentistUserEntity());
        assertEquals(dentist.getScheduleDentist(), result.getScheduleDentist());
        assertFalse(dentist.getTurnsDentist().isEmpty());
        verify(dentistService, times(1)).createDentist(dentist);

    }

    @Test
    void findDentistByIdSuccessTest (){
        Long idDentist = 1L;
        Turn turn = new Turn();
        Dentist dentist = new Dentist( "Endodoncist", new UserEntity(), new Schedule(),List.of(turn) );

        when(dentistService.findDentistById(idDentist)).thenReturn(dentist);

        ResponseEntity<?> response = dentistController.findDentistById(idDentist);

        assertEquals(OK, response.getStatusCode());
        assertEquals(dentist, response.getBody());
        verify(dentistService, times(1)).findDentistById(idDentist);
    }

    @Test
    void findDentistByIdNotFoundTest (){
        Long idDentist =1L;
        when(dentistService.findDentistById(idDentist)).thenThrow( new EntityNotFoundException("Dentist not Found"));

        ResponseEntity<?> response = dentistController.findDentistById(idDentist);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Dentist not Found", response.getBody());
        verify(dentistService, times(1)).findDentistById(idDentist);

    }
    @Test
    void findDentistByIdInternalErrorTest (){
        Long idDentist =1L;
        when(dentistService.findDentistById(idDentist)).thenThrow( new RuntimeException( "Server internal Error"));

        ResponseEntity<?> response = dentistController.findDentistById(idDentist);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(dentistService, times(1)).findDentistById(idDentist);
    }
    @Test
    void editDentsitSuccessTest(){
        Turn turn = new Turn();
        Dentist dentist = new Dentist( "Endodoncist", new UserEntity(), new Schedule(),List.of(turn) );

        when(dentistService.editDentist(any(Dentist.class))).thenReturn(dentist);

        ResponseEntity<?> response = dentistController.editDentist(dentist);

        assertEquals(OK, response.getStatusCode());
        assertEquals(dentist, response.getBody());
        verify(dentistService, times(1)).editDentist(dentist);
    }

    @Test
    void editDentistNotFoundTest (){

        Turn turn = new Turn();
        Dentist dentist = new Dentist( "Endodoncist", new UserEntity(), new Schedule(),List.of(turn) );

        when(dentistService.editDentist(dentist)).thenThrow(new EntityNotFoundException("Dentist not Found"));

        ResponseEntity<?> response = dentistController.editDentist(dentist);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Dentist not Found", response.getBody());
        verify(dentistService, times(1)).editDentist(dentist);
    }
    @Test
    void editDentistInternalErrorTest (){

        Turn turn = new Turn();
        Dentist dentist = new Dentist( "Endodoncist", new UserEntity(), new Schedule(),List.of(turn) );

        when(dentistService.editDentist(dentist)).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity<?> response = dentistController.editDentist(dentist);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(dentistService, times(1)).editDentist(dentist);
    }

    @Test
    void deleteDentistSuccessTest (){
        doNothing().when(dentistService).deleteDentistById(1L);

        ResponseEntity<?> response = dentistController.deleteDentist(1L);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Delete dentist susccessfully", response.getBody());
        verify(dentistService, times(1)).deleteDentistById(1L);
    }

    @Test
    void deleteDentistNotFoundTest (){
        doThrow(new EntityNotFoundException("Dentis not found")).when(dentistService).deleteDentistById(1L);

        ResponseEntity<?> response = dentistController.deleteDentist(1L);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Dentis not found", response.getBody());
        verify(dentistService, times(1)).deleteDentistById(1L);
    }

    @Test
    void deleteDentistInternalErrorTest (){
        doThrow(new RuntimeException("Server internal Error")).when(dentistService).deleteDentistById(1L);

        ResponseEntity<?> response = dentistController.deleteDentist(1L);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(dentistService, times(1)).deleteDentistById(1L);
    }


}
