package com.mycompany.clinica_odontologica.service.test;

import com.mycompany.clinica_odontologica.model.*;
import com.mycompany.clinica_odontologica.repository.IDentistRepository;
import com.mycompany.clinica_odontologica.repository.IScheduleRepository;
import com.mycompany.clinica_odontologica.repository.ITurnRepository;
import com.mycompany.clinica_odontologica.service.DentistService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;
public class DentistServiceTest {

    @Mock
    private IDentistRepository dentistRepository;
    @Mock
    private ITurnRepository turnRepository;
    @Mock
    private IScheduleRepository scheduleRepository;

    @InjectMocks
    private DentistService dentistService;

    @BeforeEach
    void setUp (){
        openMocks(this);
    }

    @Test
    void createDentistSuccessTest (){
        LocalDate localDate = LocalDate.now();
        List<Turn> turns = List.of(new Turn(1L, localDate, "8:00", "8:20", null, null ));
        Schedule schedule = new Schedule(1L, "8:00", "17:00", "Monday");
        String username = "test_user";
        String password = "password_test";
        Role mockRole = new Role();
        mockRole.setRoleTypeEnum(RoleEnum.SECRETARIAT);
        PermissionsEntity permissions = new PermissionsEntity(1L, "READ", null);
        RolePermissions rolePermissions = new RolePermissions(1L, mockRole, permissions);
        mockRole.setRolePermissions(Set.of(rolePermissions));


        UserEntity user1 = new UserEntity(1, username, password,  true, true, true, true, mockRole);
        Dentist dentist = new Dentist();
        dentist.setIdPerson(1L);
        dentist.setName("Jhon");
        dentist.setLastname("Doe");
        dentist.setDni("1234567");
        dentist.setNumberPhone("12334405");
        dentist.setAddress("Main Street");
        dentist.setDateOfBirth(localDate);
        dentist.setSpeciality("Endodoncist");
        dentist.setDentistUserEntity(user1);
        dentist.setScheduleDentist(schedule);
        dentist.setTurnsDentist(turns);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(dentistRepository.save(dentist)).thenReturn(dentist);

        Dentist result = dentistService.createDentist(dentist);

        assertNotNull(result);
        assertEquals(result.getIdPerson(), dentist.getIdPerson());
        assertEquals(result.getName(), dentist.getName());
        assertEquals(result.getLastname(), dentist.getLastname());
        assertEquals(result.getNumberPhone(), dentist.getNumberPhone());
        assertEquals(result.getDni(), dentist.getDni());
        assertEquals(result.getDateOfBirth(), dentist.getDateOfBirth());
        assertEquals(result.getSpeciality(), dentist.getSpeciality());
        assertEquals(result.getDentistUserEntity(), dentist.getDentistUserEntity());
        assertEquals(result.getScheduleDentist(), dentist.getScheduleDentist());
        assertEquals(result.getTurnsDentist(), dentist.getTurnsDentist());

        verify(scheduleRepository, times(1)).findById(1L);
        verify(dentistRepository, times(1)).save(dentist);

    }

    @Test
    void createDentistScheduleNotFoundTest(){
        LocalDate localDate = LocalDate.now();
        List<Turn> turns = List.of(new Turn(1L, localDate, "8:00", "8:20", null, null ));
        Schedule schedule = new  Schedule(1L, "8:00", "17:00", "Monday");
        String username = "test_user";
        String password = "password_test";
        Role mockRole = new Role();
        mockRole.setRoleTypeEnum(RoleEnum.SECRETARIAT);
        PermissionsEntity permissions = new PermissionsEntity(1L, "READ", null);
        RolePermissions rolePermissions = new RolePermissions(1L, mockRole, permissions);
        mockRole.setRolePermissions(Set.of(rolePermissions));


        UserEntity user1 = new UserEntity(1, username, password,  true, true, true, true, mockRole);
        Dentist dentist = new Dentist();
        dentist.setIdPerson(1L);
        dentist.setName("Jhon");
        dentist.setLastname("Doe");
        dentist.setDni("1234567");
        dentist.setNumberPhone("12334405");
        dentist.setAddress("Main Street");
        dentist.setDateOfBirth(localDate);
        dentist.setSpeciality("Endodoncist");
        dentist.setDentistUserEntity(user1);
        dentist.setScheduleDentist(schedule);
        dentist.setTurnsDentist(turns);

        when(scheduleRepository.findById(schedule.getIdSchedule())).thenThrow(new EntityNotFoundException("Schedule not found"));
        assertThrows(EntityNotFoundException.class,()-> dentistService.createDentist(dentist));

        verify(scheduleRepository, times(1)).findById(1L);
        verify(dentistRepository, never()).save(dentist);


    }

    @Test
    void createDentistWithoutScheduleTest() {
        // Arrange
        Dentist dentist = new Dentist();
        dentist.setScheduleDentist(null); // Schedule is null

        when(dentistRepository.save(any(Dentist.class))).thenReturn(dentist);

        // Act
        Dentist result = dentistService.createDentist(dentist);

        // Assert
        assertNull(result.getScheduleDentist());
        verify(scheduleRepository, never()).findById(anyLong());
        verify(dentistRepository, times(1)).save(dentist);
    }

    @Test
    void getDentsitEmptyTest (){

        List<Dentist> dentists = new ArrayList<>();

        when(dentistRepository.findAll()).thenReturn(dentists);

        List<Dentist> result = dentistService.getDentists();

        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        verify(dentistRepository, times(1)).findAll();
    }

    @Test
    void getDentistTest (){

        Dentist dentist = new Dentist();
        Dentist dentist2 = new Dentist();
        List<Dentist> dentists = List.of(dentist2, dentist);

        when(dentistRepository.findAll()).thenReturn(dentists);

        List<Dentist> result = dentistService.getDentists();

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        verify(dentistRepository, times(1)).findAll();

    }

    @Test
    void findDentistByIdNotFoundTest (){
        Long idDentist = 1L;

        when(dentistRepository.findById(idDentist)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                ()->dentistService.findDentistById(idDentist));

        verify(dentistRepository, times(1)).findById(idDentist) ;
    }

    @Test
    void findDentistByIdTest (){
        Long idDentist =1L;
        Dentist dentist = new Dentist("Endoncist", new UserEntity(), new Schedule(), new ArrayList<>());

        when(dentistRepository.findById(idDentist)).thenReturn(Optional.of(dentist));

        Dentist result = dentistService.findDentistById(idDentist);

        assertEquals(dentist.getSpeciality(), result.getSpeciality());
        assertEquals(dentist.getDentistUserEntity(), result.getDentistUserEntity());
        assertEquals(dentist.getScheduleDentist(), result.getScheduleDentist());
        assertEquals(dentist.getTurnsDentist(), result.getTurnsDentist());
        verify(dentistRepository, times(1)).findById(idDentist);

    }

    @Test
    void deleteDentistByIdNotFoundTest (){
        Long idDentist = 1L;
        when(dentistRepository.existsById(idDentist)).thenReturn(false);
        doThrow(new EntityNotFoundException("Dentist not found")).when(dentistRepository).deleteById(idDentist);

        assertThrows(EntityNotFoundException.class,()-> dentistService.deleteDentistById(idDentist));
        verify(dentistRepository, times(1)).existsById(idDentist);
        verify(dentistRepository, never()).deleteById(idDentist);
    }

    @Test
    void deleteDentistByIdSuccessTest (){
        Long idDentist = 1L;
        when(dentistRepository.existsById(idDentist)).thenReturn(true);
        doNothing().when(dentistRepository).deleteById(idDentist);

        dentistService.deleteDentistById(idDentist);

        verify(dentistRepository, times(1)).existsById(idDentist);
        verify(dentistRepository, times(1)).deleteById(idDentist);
    }

    @Test
    void editDentistNotFoundTest(){
        Long idDentist = 1L;

        Dentist dentist = new Dentist();
        dentist.setIdPerson(idDentist);

        when(dentistRepository.findById(dentist.getIdPerson())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                ()->dentistService.editDentist(dentist));

        verify(dentistRepository, times(1)).findById(dentist.getIdPerson());
        verify(dentistRepository, never()).save(dentist);
    }

    @Test
    void ediDentistSuccess(){
        Long idDentist = 1L;
        LocalDate localDate = LocalDate.now();
        Schedule schedule = new  Schedule(1L, "8:00", "17:00", "Monday");
        String username = "test_user";
        String password = "password_test";
        Role mockRole = new Role();
        mockRole.setRoleTypeEnum(RoleEnum.SECRETARIAT);
        PermissionsEntity permissions = new PermissionsEntity(1L, "READ", null);
        RolePermissions rolePermissions = new RolePermissions(1L, mockRole, permissions);
        mockRole.setRolePermissions(Set.of(rolePermissions));
        UserEntity user1 = new UserEntity(1, username, password,  true, true, true, true, mockRole);
        List<Turn> turns = List.of(new Turn(1L, localDate, "8:00", "8:20", null, null ));

        Dentist dentist = new Dentist();
        dentist.setIdPerson(idDentist); dentist.setName("Jhon");
        dentist.setLastname("Doe");
        dentist.setDni("1234567");
        dentist.setNumberPhone("12334405");
        dentist.setAddress("Main Street");
        dentist.setDateOfBirth(localDate);
        dentist.setSpeciality("Endodoncist");
        dentist.setDentistUserEntity(user1);
        dentist.setScheduleDentist(schedule);
        dentist.setTurnsDentist(turns);

        Dentist updateDensits = new Dentist();


        updateDensits.setIdPerson(1L);
        updateDensits.setName("Jhon");
        updateDensits.setLastname("Doe");
        updateDensits.setDni("1234567");
        updateDensits.setNumberPhone("12334405");
        updateDensits.setAddress("Main Street");
        updateDensits.setDateOfBirth(localDate);
        updateDensits.setSpeciality("Endodoncist");
        updateDensits.setDentistUserEntity(user1);
        updateDensits.setScheduleDentist(schedule);
        updateDensits.setTurnsDentist(turns);

        when(scheduleRepository.findById(schedule.getIdSchedule())).thenReturn(Optional.of(schedule));
        when(dentistRepository.findById(dentist.getIdPerson())).thenReturn(Optional.of(dentist));
        when(dentistRepository.save(dentist)).thenReturn(updateDensits);

        Dentist result = dentistService.editDentist(dentist);

        verify(dentistRepository, times(1)).findById(dentist.getIdPerson());
        verify(dentistRepository, times(1)).save(dentist);

        assertNotNull(result);
        assertEquals(updateDensits.getIdPerson(), result.getIdPerson());
        assertEquals(updateDensits.getName(), result.getName());
        assertEquals(updateDensits.getLastname(), result.getLastname());
        assertEquals(updateDensits.getNumberPhone(), result.getNumberPhone());
        assertEquals(updateDensits.getDni(), result.getDni());
        assertEquals(updateDensits.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(updateDensits.getSpeciality(), result.getSpeciality());
        assertEquals(updateDensits.getDentistUserEntity(),result.getDentistUserEntity());
        assertEquals(updateDensits.getScheduleDentist(), result.getScheduleDentist());
        assertEquals(updateDensits.getTurnsDentist(), result.getTurnsDentist());
    }






}
