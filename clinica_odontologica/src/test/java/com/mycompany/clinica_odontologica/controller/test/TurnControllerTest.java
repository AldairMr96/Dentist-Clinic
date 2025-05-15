package com.mycompany.clinica_odontologica.controller.test;

import com.mycompany.clinica_odontologica.controller.TurnController;
import com.mycompany.clinica_odontologica.model.Dentist;
import com.mycompany.clinica_odontologica.model.Patient;
import com.mycompany.clinica_odontologica.model.Schedule;
import com.mycompany.clinica_odontologica.model.Turn;
import com.mycompany.clinica_odontologica.service.ITurnService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;
import static org.springframework.http.HttpStatus.*;
public class TurnControllerTest {

    @Mock
    private ITurnService turnService;

    @InjectMocks
    private TurnController turnController;

    @BeforeEach
    void setUp (){
        openMocks(this);
    }
    @Test
    void getTurnsTest(){
        Dentist dentist = new Dentist();
        Patient patient = new Patient();
        LocalDate localDate = LocalDate.now();
        Turn turn = new Turn(1L, localDate, "8:00", "Gengivitis", dentist, patient);
        List<Turn> turns = List.of(turn);

        when(turnService.getTurn()).thenReturn(turns);

        List<Turn> result = turnController.getTurn();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        verify(turnService,  times(1)).getTurn();
    }
    @Test
    void getTurnEmptyTest(){

        List<Turn> turns = new ArrayList<>();

        when(turnService.getTurn()).thenReturn(turns);

        List<Turn> result = turnController.getTurn();

        assertNotNull(result);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        verify(turnService,  times(1)).getTurn();
    }

    @Test
    void createTurnTest(){

        Dentist dentist = new Dentist();
        Patient patient = new Patient();
        LocalDate localDate = LocalDate.now();
        Turn turn = new Turn(1L, localDate, "8:00", "Gengivitis", dentist, patient);

        when(turnService.createTurn(any(Turn.class))).thenReturn(turn);

        Turn result = turnController.createTurn(turn);

        assertNotNull(result);
        assertEquals(turn.getIdTurn(), result.getIdTurn());
        assertEquals(turn.getDateTurn(), result.getDateTurn());
        assertEquals(turn.getShiftTime(), result.getShiftTime());
        assertEquals(turn.getDisease(), result.getDisease());
        assertEquals(turn.getDentist(), result.getDentist());
        assertEquals(turn.getPatient(), result.getPatient());
        verify(turnService, times(1)).createTurn(turn);
    }

    @Test
    void findTurnByIdSuccessTest(){
        Long idTurn =1L;
        Dentist dentist = new Dentist();
        Patient patient = new Patient();
        LocalDate localDate = LocalDate.now();
        Turn turn = new Turn(1L, localDate, "8:00", "Gengivitis", dentist, patient);

        when(turnService.finTurnById(idTurn)).thenReturn(turn) ;

        ResponseEntity<?> response = turnController.findTurnById(idTurn);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(turn, response.getBody());
        verify(turnService, times(1)).finTurnById(idTurn);
    }

    @Test
    void findTurnByIdNotFoundTest(){
        Long idTurn =1L;

        when(turnService.finTurnById(idTurn)).thenThrow(new EntityNotFoundException("Turn not found")) ;

        ResponseEntity<?> response = turnController.findTurnById(idTurn);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Turn not found", response.getBody());
        verify(turnService, times(1)).finTurnById(idTurn);
    }

    @Test
    void findTurnByIdInternalErrorTest(){
        Long idTurn =1L;

        when(turnService.finTurnById(idTurn)).thenThrow(new RuntimeException("Server internal Error")) ;

        ResponseEntity<?> response = turnController.findTurnById(idTurn);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(turnService, times(1)).finTurnById(idTurn);
    }

    @Test
    void editTurnSuccessTest (){
        Dentist dentist = new Dentist();
        Patient patient = new Patient();
        LocalDate localDate = LocalDate.now();
        Turn turn = new Turn(1L, localDate, "8:00", "Gengivitis", dentist, patient);

        when(turnService.editTurn(any(Turn.class))).thenReturn(turn);

        ResponseEntity<?> response = turnController.editTurn(turn);
        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(turn, response.getBody());
        verify(turnService, times(1)).editTurn(turn);

    }

    @Test
    void editTurnNotFoundTest (){
        Dentist dentist = new Dentist();
        Patient patient = new Patient();
        LocalDate localDate = LocalDate.now();
        Turn turn = new Turn(1L, localDate, "8:00", "Gengivitis", dentist, patient);

        when(turnService.editTurn(turn)).thenThrow(new EntityNotFoundException("Turn not found"));

        ResponseEntity<?> response = turnController.editTurn(turn);
        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Turn not found", response.getBody());
        verify(turnService, times(1)).editTurn(turn);

    }
    @Test
    void editTurnInternalErrorTest (){
        Dentist dentist = new Dentist();
        Patient patient = new Patient();
        LocalDate localDate = LocalDate.now();
        Turn turn = new Turn(1L, localDate, "8:00", "Gengivitis", dentist, patient);

        when(turnService.editTurn(turn)).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity<?> response = turnController.editTurn(turn);
        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(turnService, times(1)).editTurn(turn);
    }

    @Test
    void deleteTurnSuccessTest (){
        Long idTurn =1L;

        doNothing().when(turnService).deleteTurnById(idTurn);

        ResponseEntity<?> response = turnController.deleteTurn(idTurn);
        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Delete turn susccessfully", response.getBody());
        verify(turnService, times(1)).deleteTurnById(idTurn);
    }

    @Test
    void deleteTurnNotFoundTest (){
        Long idTurn =1L;

        doThrow(new EntityNotFoundException("Turn not found")).when(turnService).deleteTurnById(idTurn);

        ResponseEntity<?> response = turnController.deleteTurn(idTurn);
        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Turn not found", response.getBody());
        verify(turnService, times(1)).deleteTurnById(idTurn);
    }
    @Test
    void deleteScheduleInternalErrorTest (){
        Long idTurn =1L;

        doThrow(new RuntimeException("Server internal Error")).when(turnService).deleteTurnById(idTurn);

        ResponseEntity<?> response = turnController.deleteTurn(idTurn);
        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(turnService, times(1)).deleteTurnById(idTurn);
    }
}
