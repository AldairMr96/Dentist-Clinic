package com.mycompany.clinica_odontologica.service.test;


import com.mycompany.clinica_odontologica.model.*;
import com.mycompany.clinica_odontologica.repository.IPatientRepository;
import com.mycompany.clinica_odontologica.repository.ISecretariatRepository;
import com.mycompany.clinica_odontologica.service.SecretariatService;
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
public class SecretariatServiceTest {

    @Mock
    private ISecretariatRepository secretariatRepository;
    @Mock
    private IPatientRepository patientRepository;

    @InjectMocks
    private SecretariatService secretariatService;

    @BeforeEach
    void setUp (){
        openMocks(this);

    }

    @Test
    void getSecretariatsEmptyTest (){
        List<Secretariat> secretariatList = new ArrayList<>();

        when(secretariatRepository.findAll()).thenReturn(Collections.emptyList());

        List<Secretariat>  result = secretariatService.getSecretariats();

        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        verify(secretariatRepository, times(1)).findAll();
    }

    @Test
    void getSecretariatsTest (){
        LocalDate localDate = LocalDate.now();
        String username = "test_user";
        String password = "password_test";
        Role mockRole = new Role();
        mockRole.setRoleTypeEnum(RoleEnum.SECRETARIAT);
        PermissionsEntity permissions = new PermissionsEntity(1L, "READ", null);
        RolePermissions rolePermissions = new RolePermissions(1L, mockRole, permissions);
        mockRole.setRolePermissions(Set.of(rolePermissions));
        UserEntity user1 = new UserEntity(1, username, password, true, true, true, true, mockRole);
        Secretariat secretariat = new Secretariat();
        secretariat.setIdPerson(1L);
        secretariat.setName("Jhon");
        secretariat.setLastname("Doe");
        secretariat.setNumberPhone("3133313131");
        secretariat.setAddress("main Street");
        secretariat.setDateOfBirth(localDate);
        secretariat.setSector("privado");
        secretariat.setSecretariatUserEntity(user1);

        List<Secretariat> secretariats = List.of(secretariat);

        when(secretariatRepository.findAll()).thenReturn(secretariats);

        List<Secretariat>  result = secretariatService.getSecretariats();

        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        verify(secretariatRepository, times(1)).findAll();


    }

    @Test
    void createSecretariatTest (){
        LocalDate localDate = LocalDate.now();
        String username = "test_user";
        String password = "password_test";
        Role mockRole = new Role();
        mockRole.setRoleTypeEnum(RoleEnum.SECRETARIAT);
        PermissionsEntity permissions = new PermissionsEntity(1L, "READ", null);
        RolePermissions rolePermissions = new RolePermissions(1L, mockRole, permissions);
        mockRole.setRolePermissions(Set.of(rolePermissions));
        UserEntity user1 = new UserEntity(1, username, password, true, true, true, true, mockRole);
        Secretariat secretariat = new Secretariat();
        secretariat.setIdPerson(1L);
        secretariat.setName("Jhon");
        secretariat.setLastname("Doe");
        secretariat.setNumberPhone("3133313131");
        secretariat.setAddress("main Street");
        secretariat.setDateOfBirth(localDate);
        secretariat.setSector("privado");
        secretariat.setSecretariatUserEntity(user1);

        when(secretariatRepository.save(any(Secretariat.class))).thenReturn(secretariat);

        Secretariat result = secretariatService.createSecretariat(secretariat);

        assertEquals(secretariat.getIdPerson(), result.getIdPerson());
        assertEquals(secretariat.getName(), result.getName());
        assertEquals(secretariat.getLastname(), result.getLastname());
        assertEquals(secretariat.getNumberPhone(), result.getNumberPhone());
        assertEquals(secretariat.getAddress(), result.getAddress());
        assertEquals(secretariat.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(secretariat.getSector(), result.getSector());
        assertEquals(secretariat.getSecretariatUserEntity(), result.getSecretariatUserEntity());
        verify(secretariatRepository, times(1)).save(any(Secretariat.class));
    }
    @Test
    void findSecretariatByIdNotFoundTest() {
        Long idSecretariat = 1L;

        when(secretariatRepository.findById(idSecretariat)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                secretariatService.findSecretariatById(idSecretariat));
        verify(secretariatRepository, times(1)).findById(idSecretariat);
    }

    @Test
    void findSecretariatByIdTest (){
        LocalDate localDate = LocalDate.now();
        String username = "test_user";
        String password = "password_test";
        Role mockRole = new Role();
        mockRole.setRoleTypeEnum(RoleEnum.SECRETARIAT);
        PermissionsEntity permissions = new PermissionsEntity(1L, "READ", null);
        RolePermissions rolePermissions = new RolePermissions(1L, mockRole, permissions);
        mockRole.setRolePermissions(Set.of(rolePermissions));
        UserEntity user1 = new UserEntity(1, username, password, true, true, true, true, mockRole);
        Secretariat secretariat = new Secretariat();
        secretariat.setIdPerson(1L);
        secretariat.setName("Jhon");
        secretariat.setLastname("Doe");
        secretariat.setNumberPhone("3133313131");
        secretariat.setAddress("main Street");
        secretariat.setDateOfBirth(localDate);
        secretariat.setSector("privado");
        secretariat.setSecretariatUserEntity(user1);

        when(secretariatRepository.findById(anyLong())).thenReturn(Optional.of(secretariat));

        Secretariat result =  secretariatService.findSecretariatById(1L);

        assertEquals(secretariat.getIdPerson(), result.getIdPerson());
        assertEquals(secretariat.getName(), result.getName());
        assertEquals(secretariat.getLastname(), result.getLastname());
        assertEquals(secretariat.getNumberPhone(), result.getNumberPhone());
        assertEquals(secretariat.getAddress(), result.getAddress());
        assertEquals(secretariat.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(secretariat.getSector(), result.getSector());
        assertEquals(secretariat.getSecretariatUserEntity(), result.getSecretariatUserEntity());
        verify(secretariatRepository, times(1)).findById(anyLong());

    }


    @Test
    void deleteSecretariatByIdNotFoundTest() {
        Long idSecretariat = 1L;
        when(secretariatRepository.existsById(idSecretariat)).thenReturn(false);
        doThrow(new EntityNotFoundException("Secretariat not found")).when(patientRepository).deleteById(idSecretariat);

        assertThrows(EntityNotFoundException.class, () ->
                secretariatService.deleteSecretariatById(idSecretariat));

        verify(secretariatRepository, times(1)).existsById(idSecretariat);
        verify(secretariatRepository, never()).deleteById(idSecretariat);
    }

    @Test
    void deleteSecretariatByIdTest() {
        Long idSecretariat = 1L;
        when(secretariatRepository.existsById(idSecretariat)).thenReturn(true);
        doNothing().when(secretariatRepository).deleteById(idSecretariat);

        secretariatService.deleteSecretariatById(idSecretariat);

        verify(secretariatRepository, times(1)).existsById(idSecretariat);
        verify(secretariatRepository, times(1)).deleteById(idSecretariat);
    }

    @Test
    void editSecretariatNotFoundTest (){
        Long idSecretariat = 1L;
        Secretariat secretariat = new Secretariat();
        secretariat.setIdPerson(idSecretariat);
        when(secretariatRepository.findById(secretariat.getIdPerson())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> secretariatService.editSecretariat(secretariat));

        verify(secretariatRepository, times(1)).findById(secretariat.getIdPerson());
        verify(secretariatRepository, never()).save(secretariat);
    }

    @Test
    void editSecretariatTest (){
        LocalDate localDate = LocalDate.now();
        String username = "test_user";
        String password = "password_test";
        Role mockRole = new Role();
        mockRole.setRoleTypeEnum(RoleEnum.SECRETARIAT);
        PermissionsEntity permissions = new PermissionsEntity(1L, "READ", null);
        RolePermissions rolePermissions = new RolePermissions(1L, mockRole, permissions);
        mockRole.setRolePermissions(Set.of(rolePermissions));
        UserEntity user1 = new UserEntity(1, username, password, true, true, true, true, mockRole);
        Secretariat secretariat = new Secretariat();
        secretariat.setIdPerson(1L);
        secretariat.setName("Jhon");
        secretariat.setLastname("Doe");
        secretariat.setNumberPhone("3133313131");
        secretariat.setAddress("main Street");
        secretariat.setDateOfBirth(localDate);
        secretariat.setSector("privado");
        secretariat.setSecretariatUserEntity(user1);

        Secretariat updateSecretariat = new Secretariat();
        updateSecretariat.setIdPerson(1L);
        updateSecretariat.setName("Jhonny");
        updateSecretariat.setLastname("Does");
        updateSecretariat.setNumberPhone("3133313131");
        updateSecretariat.setAddress("main Street");
        updateSecretariat.setDateOfBirth(localDate);
        updateSecretariat.setSector("privado");
        updateSecretariat.setSecretariatUserEntity(user1);

        when(secretariatRepository.findById(anyLong())).thenReturn(Optional.of(secretariat));
        when(secretariatRepository.save(secretariat)).thenReturn(updateSecretariat);

        Secretariat result = secretariatService.editSecretariat(updateSecretariat);

        assertEquals(updateSecretariat.getIdPerson(), result.getIdPerson());
        assertEquals(updateSecretariat.getName(), result.getName());
        assertEquals(updateSecretariat.getLastname(), result.getLastname());
        assertEquals(updateSecretariat.getNumberPhone(), result.getNumberPhone());
        assertEquals(updateSecretariat.getAddress(), result.getAddress());
        assertEquals(updateSecretariat.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(updateSecretariat.getSector(), result.getSector());
        assertEquals(updateSecretariat.getSecretariatUserEntity(), result.getSecretariatUserEntity());
        verify(secretariatRepository, times(1)).findById(anyLong());
        verify(secretariatRepository, times(1)).save(secretariat);
    }

    @Test
    void getPatientsPerDayEmptyTest (){

        LocalDate localDate = LocalDate.now();
        when(patientRepository.findDistinctByTurnsPatient_DateTurn(localDate)).thenReturn(Collections.emptyList());

        List<Patient> result = secretariatService.getPatientsPerDay(localDate);

        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(patientRepository, times(1)).findDistinctByTurnsPatient_DateTurn(localDate);
    }

    @Test
    void  getPatientsPerDayTest (){
        Patient patient = new Patient();
        Long idPatient = 1L;
        LocalDate localDate = LocalDate.now();
        Turn turn = new Turn(1L, localDate, "8:00", "8:20", null, null);
        List<Turn> turns = List.of(turn);
        String username = "test_user";
        String password = "password_test";
        Role mockRole = new Role();
        mockRole.setRoleTypeEnum(RoleEnum.SECRETARIAT);
        PermissionsEntity permissions = new PermissionsEntity(1L, "READ", null);
        RolePermissions rolePermissions = new RolePermissions(1L, mockRole, permissions);
        mockRole.setRolePermissions(Set.of(rolePermissions));
        UserEntity user1 = new UserEntity(1, username, password, true, true, true, true, mockRole);
        Responsible responsible = new Responsible();
        responsible.setIdPerson(1L);
        responsible.setName("Jhon");
        responsible.setLastname("Doe");
        responsible.setNumberPhone("3133313131");
        responsible.setAddress("main Street");
        responsible.setDateOfBirth(localDate);
        responsible.setRelationshipType("Father");
        List<Responsible> responsibles = List.of(responsible);

        //Patient
        patient.setIdPerson(idPatient);
        patient.setName("Jane");
        patient.setLastname("More");
        patient.setNumberPhone("1392378964");
        patient.setAddress("Walk Street");
        patient.setDateOfBirth(localDate);
        patient.setMedicalInsuranceType(MedicalInsuranceTypeEnum.FREE);
        patient.setBloodType("O+");
        patient.setResponsibles(responsibles);
        patient.setTurnsPatient(turns);
        patient.setUserEntityPatient(user1);

        List<Patient> patients = List.of(patient);

        when(patientRepository.findDistinctByTurnsPatient_DateTurn(localDate)).thenReturn(patients);
        List<Patient> result = secretariatService.getPatientsPerDay(localDate);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(patientRepository, times(1)).findDistinctByTurnsPatient_DateTurn(localDate);

    }


}
