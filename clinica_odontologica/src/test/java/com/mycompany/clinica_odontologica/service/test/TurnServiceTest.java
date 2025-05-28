package com.mycompany.clinica_odontologica.service.test;

import com.mycompany.clinica_odontologica.model.Dentist;
import com.mycompany.clinica_odontologica.model.Patient;
import com.mycompany.clinica_odontologica.model.Turn;
import com.mycompany.clinica_odontologica.repository.IDentistRepository;
import com.mycompany.clinica_odontologica.repository.ITurnRepository;
import com.mycompany.clinica_odontologica.service.TurnService;
import com.mysql.cj.xdevapi.TableImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;
public class TurnServiceTest {

    @Mock
    private ITurnRepository turnRepository;

    @Mock
    private IDentistRepository dentistRepository;

    @InjectMocks
    private TurnService turnService;

    @BeforeEach
    void setUp (){
        openMocks(this);
    }

    @Test
    void createTurnTest (){
        LocalDate localDate = LocalDate.now();
        Turn turn = new Turn();
        Dentist dentist = new Dentist();
        Patient patient = new Patient();

        turn.setIdTurn(1L);
        turn.setDateTurn(localDate);
        turn.setShiftTime("8:20");
        turn.setDisease("Gengivitys");
        turn.setDentist(dentist);
        turn.setPatient(patient);

        when(turnRepository.save(turn)).thenReturn(turn);

        Turn result = turnService.createTurn(turn);

        assertEquals(turn.getIdTurn(), result.getIdTurn());
        assertEquals(turn.getDateTurn(), result.getDateTurn());
        assertEquals(turn.getShiftTime(), result.getShiftTime());
        assertEquals(turn.getDisease(), result.getDisease());
        assertEquals(turn.getDentist(), result.getDentist());
        assertEquals(turn.getPatient(), result.getPatient());
        verify(turnRepository, times(1)).save(turn);

    }

    @Test
    void getTurnsEmptyList (){

        when(turnRepository.findAll()).thenReturn(Collections.emptyList());

        List<Turn> result = turnService.getTurn();

        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        verify(turnRepository, times(1)).findAll();
    }
    @Test
    void getTurnsList (){
        LocalDate localDate = LocalDate.now();
        Turn turn = new Turn();
        Dentist dentist = new Dentist();
        Patient patient = new Patient();

        turn.setIdTurn(1L);
        turn.setDateTurn(localDate);
        turn.setShiftTime("8:20");
        turn.setDisease("Gengivitys");
        turn.setDentist(dentist);
        turn.setPatient(patient);
        List<Turn> turns = List.of(turn);
        when(turnRepository.findAll()).thenReturn(turns);

        List<Turn> result = turnService.getTurn();

        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        verify(turnRepository, times(1)).findAll();
    }

    @Test
    void findTurnByIdNotFoundTest (){
        Long idTurn = 1L;

        when(turnRepository.findById(idTurn)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                ()->turnService.finTurnById(idTurn));
        verify(turnRepository, times(1)).findById(idTurn);
    }

    @Test
    void findTurnByIdSuccessTest (){
        Long idTurn = 1L;
        LocalDate localDate = LocalDate.now();
        Turn turn = new Turn();
        Dentist dentist = new Dentist();
        Patient patient = new Patient();

        turn.setIdTurn(idTurn);
        turn.setDateTurn(localDate);
        turn.setShiftTime("8:20");
        turn.setDisease("Gengivitys");
        turn.setDentist(dentist);
        turn.setPatient(patient);

        when(turnRepository.findById(idTurn)).thenReturn(Optional.of(turn));

        Turn result = turnService.finTurnById(idTurn);
        assertEquals(turn.getIdTurn(), result.getIdTurn());
        assertEquals(turn.getDateTurn(), result.getDateTurn());
        assertEquals(turn.getShiftTime(), result.getShiftTime());
        assertEquals(turn.getDisease(), result.getDisease());
        assertEquals(turn.getDentist(), result.getDentist());
        assertEquals(turn.getPatient(), result.getPatient());
        verify(turnRepository, times(1)).findById(idTurn);
    }

    @Test
    void deleteTurnByIdNotExistTest (){
        Long idTurn = 1L;
        when(turnRepository.existsById(idTurn)).thenReturn(false);
        doThrow(new EntityNotFoundException("Turn not found")).when(turnRepository).deleteById(idTurn);

        assertThrows(EntityNotFoundException.class , ()-> turnService.deleteTurnById(idTurn));
        verify(turnRepository, times(1)).existsById(idTurn);
        verify(turnRepository, never()).deleteById(idTurn);

    }

    @Test
    void deleteTurnByIdSuccessTest (){
        Long idTurn = 1L;
        when(turnRepository.existsById(idTurn)).thenReturn(true);
        doNothing().when(turnRepository).deleteById(idTurn);

        turnService.deleteTurnById(idTurn);
        verify(turnRepository, times(1)).existsById(idTurn);
        verify(turnRepository, times(1)).deleteById(idTurn);
    }

    @Test
    void editTurnNotFoundTest (){
        Long idTurn = 1L;
        Turn turn = new Turn();
        turn.setIdTurn(idTurn);

        when(turnRepository.findById(idTurn)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()-> turnService.editTurn(turn));

        verify(turnRepository, times(1)).findById(idTurn);
        verify(turnRepository, never()).save(turn);
    }

    @Test
    void editTurnSuccessTest (){
        Long idTurn = 1L;
        LocalDate localDate = LocalDate.now();
        Turn turn = new Turn();
        Dentist dentist = new Dentist();
        Patient patient = new Patient();

        turn.setIdTurn(idTurn);
        turn.setDateTurn(localDate);
        turn.setShiftTime("8:20");
        turn.setDisease("Gengivitys");
        turn.setDentist(dentist);
        turn.setPatient(patient);

        Turn updateTurn = new Turn();
        updateTurn.setIdTurn(idTurn);
        updateTurn.setDateTurn(localDate);
        updateTurn.setShiftTime("8:10");
        updateTurn.setDisease("Gengivitys");
        updateTurn.setDentist(dentist);
        updateTurn.setPatient(patient);

        when(turnRepository.findById(idTurn)).thenReturn(Optional.of(turn));
        when(turnRepository.save(turn)).thenReturn(updateTurn);

        Turn result = turnService.editTurn(updateTurn);
        assertEquals(updateTurn.getIdTurn(), result.getIdTurn());
        assertEquals(updateTurn.getDateTurn(), result.getDateTurn());
        assertEquals(updateTurn.getShiftTime(), result.getShiftTime());
        assertEquals(updateTurn.getDisease(), result.getDisease());
        assertEquals(updateTurn.getDentist(), result.getDentist());
        assertEquals(updateTurn.getPatient(), result.getPatient());

        verify(turnRepository, times(1)).findById(idTurn);
        verify(turnRepository, times(1)).save(turn);

    }

    @Test
    void turnsDentistsPerDayNotFoundTest (){
        Long idDentist = 1L;
        LocalDate localDate = LocalDate.now();

        when(dentistRepository.existsById(idDentist)).thenReturn(false);


        assertThrows(EntityNotFoundException.class, ()-> turnService.turnsDentistsPerDay(idDentist, localDate));

        verify(dentistRepository, times(1)).existsById(idDentist);
        verify(turnRepository, never()).findByDentist_IdPersonAndDateTurn(idDentist, localDate);
    }

    @Test
    void turnsDentistsPerDaySuccessTest(){
        Long idTurn = 1L;
        Long idDentist = 1L;
        LocalDate localDate = LocalDate.now();
        Turn turn = new Turn();
        Dentist dentist = new Dentist();
        dentist.setIdPerson(idDentist);
        Patient patient = new Patient();

        turn.setIdTurn(idTurn);
        turn.setDateTurn(localDate);
        turn.setShiftTime("8:20");
        turn.setDisease("Gengivitys");
        turn.setDentist(dentist);
        turn.setPatient(patient);

        List<Turn> turns = List.of(turn);

        when(dentistRepository.existsById(idDentist)).thenReturn(true);
        when(turnRepository.findByDentist_IdPersonAndDateTurn(idDentist, localDate)).thenReturn(turns);

        List<Turn> result = turnService.turnsDentistsPerDay(idDentist, localDate);

        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        verify(dentistRepository, times(1)).existsById(idDentist);
        verify(turnRepository, times(1)).findByDentist_IdPersonAndDateTurn(idDentist, localDate);
    }
}
