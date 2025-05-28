package com.mycompany.clinica_odontologica.service.test;
import com.mycompany.clinica_odontologica.model.Patient;
import com.mycompany.clinica_odontologica.model.Responsible;
import com.mycompany.clinica_odontologica.repository.IResponsibleRepository;
import com.mycompany.clinica_odontologica.service.ResponsibleService;
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
public class ResponsibleServiceTest {

    @Mock
    private IResponsibleRepository responsibleRepository;

    @InjectMocks
    private ResponsibleService responsibleService;

    @BeforeEach
    void setUp (){
        openMocks(this);
    }

    @Test
    void createResponsibleSuccessTest (){
        LocalDate localDate = LocalDate.now();
        Responsible responsible = new Responsible();
        responsible.setIdPerson(1L);
        responsible.setName("Jhon");
        responsible.setLastname("Doe");
        responsible.setNumberPhone("3133313131");
        responsible.setAddress("main Street");
        responsible.setDateOfBirth(localDate);
        responsible.setRelationshipType("Father");
        responsible.setRelationshipPatient(new Patient());

        when(responsibleRepository.save(any(Responsible.class))).thenReturn(responsible);

        Responsible result = responsibleService.createResponsible(responsible);

        assertNotNull(result);
        assertEquals(responsible.getIdPerson(), result.getIdPerson());
        assertEquals(responsible.getName(), result.getName());
        assertEquals(responsible.getLastname(), result.getLastname());
        assertEquals(responsible.getNumberPhone(), result.getNumberPhone());
        assertEquals(responsible.getAddress(), result.getAddress());
        assertEquals(responsible.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(responsible.getRelationshipType(), result.getRelationshipType());
        assertEquals(responsible.getRelationshipPatient(), result.getRelationshipPatient());

        verify(responsibleRepository, times(1)).save(responsible);
    }

    @Test
    void getResponsiblesEmptyTest (){

        when(responsibleRepository.findAll()).thenReturn(Collections.emptyList());

        List<Responsible> result = responsibleService.getResponsibles();

        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        verify(responsibleRepository, times(1)).findAll();
    }

    @Test
    void getResponsiblesSuccessTest (){
        LocalDate localDate = LocalDate.now();
        Responsible responsible = new Responsible();
        responsible.setIdPerson(1L);
        responsible.setName("Jhon");
        responsible.setLastname("Doe");
        responsible.setNumberPhone("3133313131");
        responsible.setAddress("main Street");
        responsible.setDateOfBirth(localDate);
        responsible.setRelationshipType("Father");
        responsible.setRelationshipPatient(new Patient());
        List<Responsible> responsibles = List.of(responsible);

        when(responsibleRepository.findAll()).thenReturn(responsibles);

        List<Responsible> result = responsibleService.getResponsibles();

        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        verify(responsibleRepository, times(1)).findAll();
    }

    @Test
    void findResponsibleByIdNotFoundTest (){
        Long idResponsible = 1L ;

        when(responsibleRepository.findById(idResponsible)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                ()->responsibleService.finResponsibletById(idResponsible));

        verify(responsibleRepository, times(1)).findById(idResponsible);
    }

    @Test
    void findResponsibleByIdSuccessTest (){
        Long idResponsible = 1L ;
        LocalDate localDate = LocalDate.now();
        Responsible responsible = new Responsible();
        responsible.setIdPerson(idResponsible);
        responsible.setName("Jhon");
        responsible.setLastname("Doe");
        responsible.setNumberPhone("3133313131");
        responsible.setAddress("main Street");
        responsible.setDateOfBirth(localDate);
        responsible.setRelationshipType("Father");
        responsible.setRelationshipPatient(new Patient());

        when(responsibleRepository.findById(idResponsible)).thenReturn(Optional.of(responsible));

        Responsible result = responsibleService.finResponsibletById(idResponsible);
        assertNotNull(result);
        assertEquals(responsible.getIdPerson(), result.getIdPerson());
        assertEquals(responsible.getName(), result.getName());
        assertEquals(responsible.getLastname(), result.getLastname());
        assertEquals(responsible.getNumberPhone(), result.getNumberPhone());
        assertEquals(responsible.getAddress(), result.getAddress());
        assertEquals(responsible.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(responsible.getRelationshipType(), result.getRelationshipType());
        assertEquals(responsible.getRelationshipPatient(), result.getRelationshipPatient());
        verify(responsibleRepository, times(1)).findById(idResponsible);
    }

    @Test
    void deleteResponsibleByIdNotFoundTest (){
        Long idResponsible = 1L;
        when(responsibleRepository.existsById(idResponsible)).thenReturn(false);
        doThrow(new EntityNotFoundException("Responsible not found")).when(responsibleRepository).deleteById(idResponsible);

        assertThrows(EntityNotFoundException.class, () ->
                responsibleService.deleteResponsibleById(idResponsible));

        verify(responsibleRepository, times(1)).existsById(idResponsible);
        verify(responsibleRepository, never()).deleteById(idResponsible);
    }

    @Test
    void deleteResponsibleBySuccessTest (){
        Long idResponsible = 1L;
        when(responsibleRepository.existsById(idResponsible)).thenReturn(true);
        doNothing().when(responsibleRepository).deleteById(idResponsible);

        responsibleService.deleteResponsibleById(idResponsible);

        verify(responsibleRepository, times(1)).existsById(idResponsible);
        verify(responsibleRepository, times(1)).deleteById(idResponsible);
    }

    @Test
    void editResponsibleNotFoundTest () {
        Long idResponsible = 1L;

        Responsible responsible = new Responsible();
        responsible.setIdPerson(idResponsible);

        when(responsibleRepository.findById(idResponsible)).thenReturn(Optional.empty());

        assertThrows( EntityNotFoundException.class, ()-> responsibleService.editResponsible(responsible));

        verify(responsibleRepository, times(1)).findById(idResponsible);
        verify(responsibleRepository, never()).save(responsible);

    }

    @Test
    void editResponsibleSuccessTest (){
        Long idResponsible = 1L ;
        LocalDate localDate = LocalDate.now();
        Responsible responsible = new Responsible();
        responsible.setIdPerson(idResponsible);
        responsible.setName("Jhon");
        responsible.setLastname("Doe");
        responsible.setNumberPhone("3133313131");
        responsible.setAddress("main Street");
        responsible.setDateOfBirth(localDate);
        responsible.setRelationshipType("Father");
        responsible.setRelationshipPatient(new Patient());

        Responsible updateResponsible = new Responsible();
        updateResponsible.setIdPerson(idResponsible);
        updateResponsible.setName("Jhonny");
        updateResponsible.setLastname("Does");
        updateResponsible.setNumberPhone("3133313131");
        updateResponsible.setAddress("main Street");
        updateResponsible.setDateOfBirth(localDate);
        updateResponsible.setRelationshipType("Father");
        updateResponsible.setRelationshipPatient(new Patient());

        when(responsibleRepository.findById(idResponsible)).thenReturn(Optional.of(responsible));
        when(responsibleRepository.save(responsible)).thenReturn(updateResponsible);

        Responsible result = responsibleService.editResponsible(updateResponsible);

        assertNotNull(result);


        verify(responsibleRepository, times(1)).findById(idResponsible);
        verify(responsibleRepository, times(1)).save(responsible);
    }
}
