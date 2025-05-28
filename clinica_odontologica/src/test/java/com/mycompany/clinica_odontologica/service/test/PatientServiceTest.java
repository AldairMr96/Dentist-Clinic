package com.mycompany.clinica_odontologica.service.test;

import com.mycompany.clinica_odontologica.model.*;
import com.mycompany.clinica_odontologica.repository.IPatientRepository;
import com.mycompany.clinica_odontologica.repository.IResponsibleRepository;
import com.mycompany.clinica_odontologica.repository.ITurnRepository;
import com.mycompany.clinica_odontologica.service.PatientService;
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
public class PatientServiceTest {

    @Mock
    private IPatientRepository patientRepository;
    @Mock
    private ITurnRepository turnRepository;
    @Mock
    private IResponsibleRepository responsibleRepository;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void getPatientsEmptyListTest() {
        List<Patient> patients = new ArrayList<>();

        when(patientRepository.findAll()).thenReturn(patients);

        List<Patient> result = patientService.getPatient();

        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void getPatientsTest() {
        Patient patient = new Patient();
        LocalDate localDate = LocalDate.now();
        List<Turn> turns = List.of(new Turn(1L, localDate, "8:00", "8:20", null, null));
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
        patient.setIdPerson(1L);
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

        when(patientRepository.findAll()).thenReturn(patients);

        List<Patient> result = patientService.getPatient();

        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void findPatientByIdNotFoundTest() {

        Long idPatient = 1L;

        when(patientRepository.findById(idPatient)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                patientService.findPatientById(idPatient));
        verify(patientRepository, times(1)).findById(idPatient);
    }

    @Test
    void findPatientByIdTest() {
        Patient patient = new Patient();
        Long idPatient = 1L;
        LocalDate localDate = LocalDate.now();
        List<Turn> turns = List.of(new Turn(1L, localDate, "8:00", "8:20", null, null));
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

        when(patientRepository.findById(idPatient)).thenReturn(Optional.of(patient));

        Patient result = patientService.findPatientById(idPatient);

        assertEquals(patient.getIdPerson(), result.getIdPerson());
        assertEquals(patient.getName(), result.getName());
        assertEquals(patient.getLastname(), result.getLastname());
        assertEquals(patient.getNumberPhone(), result.getNumberPhone());
        assertEquals(patient.getAddress(), result.getAddress());
        assertEquals(patient.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(patient.getMedicalInsuranceType(), result.getMedicalInsuranceType());
        assertEquals(patient.getBloodType(), result.getBloodType());
        assertEquals(patient.getResponsibles(), result.getResponsibles());
        assertEquals(patient.getTurnsPatient(), result.getTurnsPatient());
        assertEquals(patient.getUserEntityPatient(), result.getUserEntityPatient());
        verify(patientRepository, times(1)).findById(idPatient);
    }

    @Test
    void createPatientTest() {
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

        when(turnRepository.findById(turn.getIdTurn())).thenReturn(Optional.of(turn));
        when(responsibleRepository.findById(responsible.getIdPerson())).thenReturn(Optional.of(responsible));
        when(patientRepository.save(patient)).thenReturn((patient));

        Patient result = patientService.createPatient(patient);
        assertEquals(patient.getIdPerson(), result.getIdPerson());
        assertEquals(patient.getName(), result.getName());
        assertEquals(patient.getLastname(), result.getLastname());
        assertEquals(patient.getNumberPhone(), result.getNumberPhone());
        assertEquals(patient.getAddress(), result.getAddress());
        assertEquals(patient.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(patient.getMedicalInsuranceType(), result.getMedicalInsuranceType());
        assertEquals(patient.getBloodType(), result.getBloodType());
        assertEquals(patient.getResponsibles(), result.getResponsibles());
        assertEquals(patient.getTurnsPatient(), result.getTurnsPatient());
        assertEquals(patient.getUserEntityPatient(), result.getUserEntityPatient());
        verify(turnRepository, times(1)).findById(turn.getIdTurn());
        verify(responsibleRepository, times(1)).findById(responsible.getIdPerson());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void createPatientTurnNotFoundTest() {
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


        when(turnRepository.findById(turn.getIdTurn())).thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class, () ->
                patientService.createPatient(patient));
        verify(turnRepository, times(1)).findById(turn.getIdTurn());
        verify(responsibleRepository, never()).findById(responsible.getIdPerson());
        verify(patientRepository, never()).save(patient);
    }

    @Test
    void createPatientResponsibleNotFoundTest() {
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

        when(turnRepository.findById(turn.getIdTurn())).thenReturn(Optional.of(turn));
        when(responsibleRepository.findById(responsible.getIdPerson())).thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class, () ->
                patientService.createPatient(patient));

        verify(responsibleRepository, times(1)).findById(responsible.getIdPerson());
        verify(turnRepository, times(1)).findById(turn.getIdTurn());
        verify(patientRepository, never()).save(patient);
    }

    @Test
    void createPatientWhitTurnAndResponsibleNullTest() {
        Patient patient = new Patient();
        Long idPatient = 1L;
        LocalDate localDate = LocalDate.now();

        String username = "test_user";
        String password = "password_test";
        Role mockRole = new Role();
        mockRole.setRoleTypeEnum(RoleEnum.SECRETARIAT);
        PermissionsEntity permissions = new PermissionsEntity(1L, "READ", null);
        RolePermissions rolePermissions = new RolePermissions(1L, mockRole, permissions);
        mockRole.setRolePermissions(Set.of(rolePermissions));
        UserEntity user1 = new UserEntity(1, username, password, true, true, true, true, mockRole);


        //Patient
        patient.setIdPerson(idPatient);
        patient.setName("Jane");
        patient.setLastname("More");
        patient.setNumberPhone("1392378964");
        patient.setAddress("Walk Street");
        patient.setDateOfBirth(localDate);
        patient.setMedicalInsuranceType(MedicalInsuranceTypeEnum.FREE);
        patient.setBloodType("O+");
        patient.setResponsibles(null);
        patient.setTurnsPatient(null);
        patient.setUserEntityPatient(user1);

        when(patientRepository.save(patient)).thenReturn(patient);

        Patient result = patientService.createPatient(patient);

        assertEquals(patient.getIdPerson(), result.getIdPerson());
        assertEquals(patient.getName(), result.getName());
        assertEquals(patient.getLastname(), result.getLastname());
        assertEquals(patient.getNumberPhone(), result.getNumberPhone());
        assertEquals(patient.getAddress(), result.getAddress());
        assertEquals(patient.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(patient.getMedicalInsuranceType(), result.getMedicalInsuranceType());
        assertEquals(patient.getBloodType(), result.getBloodType());
        assertEquals(patient.getUserEntityPatient(), result.getUserEntityPatient());
        assertEquals(null, result.getTurnsPatient());
        assertEquals(null, result.getResponsibles());

        verify(turnRepository, never()).findById(1L);
        verify(responsibleRepository, never()).findById(1L);
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void deletePatientByIdNotFoundTest() {
        Long idPatient = 1L;
        when(patientRepository.existsById(idPatient)).thenReturn(false);
        doThrow(new EntityNotFoundException("Patient not found")).when(patientRepository).deleteById(idPatient);

        assertThrows(EntityNotFoundException.class, () ->
                patientService.deletePatientById(idPatient));

        verify(patientRepository, times(1)).existsById(idPatient);
        verify(patientRepository, never()).deleteById(idPatient);
    }

    @Test
    void deletePatientByIdTest() {
        Long idPatient = 1L;
        when(patientRepository.existsById(idPatient)).thenReturn(true);
        doNothing().when(patientRepository).deleteById(idPatient);

        patientService.deletePatientById(idPatient);

        verify(patientRepository, times(1)).existsById(idPatient);
        verify(patientRepository, times(1)).deleteById(idPatient);
    }

    @Test
    void editPatientNotFoundTest() {
        Long idPatient = 1L;

        Patient patient = new Patient();
        patient.setIdPerson(idPatient);

        when(patientRepository.findById(patient.getIdPerson())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> patientService.editPatient(patient));

        verify(patientRepository, times(1)).findById(patient.getIdPerson());
        verify(patientRepository, never()).save(patient);
    }

    @Test
    void editPatientTest() {
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

        Patient updatePatient = new Patient();
        updatePatient.setIdPerson(idPatient);
        updatePatient.setName("Jane");
        updatePatient.setLastname("More");
        updatePatient.setNumberPhone("1392378964");
        updatePatient.setAddress("Walk Street");
        updatePatient.setDateOfBirth(localDate);
        updatePatient.setMedicalInsuranceType(MedicalInsuranceTypeEnum.FREE);
        updatePatient.setBloodType("O+");
        updatePatient.setResponsibles(responsibles);
        updatePatient.setTurnsPatient(turns);
        updatePatient.setUserEntityPatient(user1);

        when(turnRepository.findById(turn.getIdTurn())).thenReturn(Optional.of(turn));
        when(responsibleRepository.findById(responsible.getIdPerson())).thenReturn(Optional.of(responsible));
        when(patientRepository.findById(patient.getIdPerson())).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(updatePatient);

        Patient result = patientService.editPatient(patient);
        assertEquals(updatePatient.getIdPerson(), result.getIdPerson());
        assertEquals(updatePatient.getName(), result.getName());
        assertEquals(updatePatient.getLastname(), result.getLastname());
        assertEquals(updatePatient.getNumberPhone(), result.getNumberPhone());
        assertEquals(updatePatient.getAddress(), result.getAddress());
        assertEquals(updatePatient.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(updatePatient.getMedicalInsuranceType(), result.getMedicalInsuranceType());
        assertEquals(updatePatient.getBloodType(), result.getBloodType());
        assertEquals(updatePatient.getResponsibles(), result.getResponsibles());
        assertEquals(updatePatient.getTurnsPatient(), result.getTurnsPatient());
        assertEquals(updatePatient.getUserEntityPatient(), result.getUserEntityPatient());

        verify(turnRepository, times(1)).findById(turn.getIdTurn());
        verify(responsibleRepository, times(1)).findById(responsible.getIdPerson());
        verify(patientRepository, times(1)).findById(patient.getIdPerson());
        verify(patientRepository, times(1)).save(patient);

    }

    @Test
    void getPatientWithMedicalInsureTest() {

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

        Patient patient1 = new Patient();

        patient1.setIdPerson(2L);
        patient1.setName("Jasmin");
        patient1.setLastname("More");
        patient1.setNumberPhone("1392378964");
        patient1.setAddress("Walk Street");
        patient1.setDateOfBirth(localDate);
        patient1.setMedicalInsuranceType(MedicalInsuranceTypeEnum.FREE);
        patient1.setBloodType("O+");
        patient1.setResponsibles(responsibles);
        patient1.setTurnsPatient(turns);
        patient1.setUserEntityPatient(user1);

        List<Patient> patients = List.of(patient1, patient);

        when(patientRepository.findAll()).thenReturn(patients);

        List<Patient> result = patientService.getPatientWithMedicalInsure(MedicalInsuranceTypeEnum.FREE.name());

        assertEquals(2, result.size());
        assertFalse(result.isEmpty());
        assertTrue(patient.getMedicalInsuranceType().name().equals(MedicalInsuranceTypeEnum.FREE.name()));
        assertTrue(result.contains(patient1));
        assertTrue(result.contains(patient));
        verify(patientRepository, times(1)).findAll();

    }

    @Test
    void getPatientWithMedicalInsureNotCoincidenceTest() {
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


        when(patientRepository.findAll()).thenReturn(patients);

        List<Patient> result = patientService.getPatientWithMedicalInsure(MedicalInsuranceTypeEnum.PREPAID.name());

        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        assertNotEquals(patient.getMedicalInsuranceType().name(), (MedicalInsuranceTypeEnum.PREPAID.name()));
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void getPatientWithMedicalInsureEmptyTest(){
        // Arrange
        String insuranceType = "FREE";

        when(patientRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Patient> result = patientService.getPatientWithMedicalInsure(insuranceType);

        // Assert
        assertTrue(result.isEmpty());
        verify(patientRepository, times(1)).findAll();

    }




}
