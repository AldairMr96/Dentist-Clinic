package com.mycompany.clinica_odontologica.controller.test;

import com.mycompany.clinica_odontologica.controller.ScheduleController;
import com.mycompany.clinica_odontologica.model.Schedule;
import com.mycompany.clinica_odontologica.repository.IScheduleRepository;
import com.mycompany.clinica_odontologica.service.IScheduleService;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Internal;
import org.hibernate.validator.constraints.CreditCardNumber;
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
public class ScheduleControllerTest {

    @Mock
    private IScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    @BeforeEach
    void setUp (){
        openMocks(this);
    }

    @Test
    void getSchedulesTest(){
        Schedule schedule = new Schedule(1L, "8:00", "17:00", "MONDAY-FRIDAY");
        List<Schedule> schedules = List.of(schedule);

        when(scheduleService.getSchedule()).thenReturn(schedules);

        List<Schedule> result = scheduleController.getSchedule();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        verify(scheduleService,  times(1)).getSchedule();
    }
    @Test
    void getSchedulesEmptyTest(){

        List<Schedule> schedules = new ArrayList<>();

        when(scheduleService.getSchedule()).thenReturn(schedules);

        List<Schedule> result = scheduleController.getSchedule();

        assertNotNull(result);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        verify(scheduleService,  times(1)).getSchedule();
    }

    @Test
    void createScheduleTest(){

        Schedule schedule = new Schedule(1L, "8:00", "17:00", "MONDAY-FRIDAY");

        when(scheduleService.createSchedule(any(Schedule.class))).thenReturn(schedule);

        Schedule result = scheduleController.createSchedule(schedule);

        assertNotNull(result);
        assertEquals(schedule.getIdSchedule(), result.getIdSchedule());
        assertEquals(schedule.getStarTime(), result.getStarTime());
        assertEquals(schedule.getTimeOver(), result.getTimeOver());
        assertEquals(schedule.getWorkingDays(), result.getWorkingDays());
        verify(scheduleService, times(1)).createSchedule(schedule);
    }

    @Test
    void findScheduleByIdSuccessTest(){
        Long idSchedule =1L;
        Schedule schedule = new Schedule(1L, "8:00", "17:00", "MONDAY-FRIDAY");

        when(scheduleService.findScheduleById(idSchedule)).thenReturn(schedule) ;

        ResponseEntity<?> response = scheduleController.findScheduleById(idSchedule);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(schedule, response.getBody());
        verify(scheduleService, times(1)).findScheduleById(idSchedule);
    }

    @Test
    void findScheduleByIdNotFoundTest(){
        Long idSchedule =1L;

        when(scheduleService.findScheduleById(idSchedule)).thenThrow(new EntityNotFoundException("Schedule not found")) ;

        ResponseEntity<?> response = scheduleController.findScheduleById(idSchedule);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Schedule not found", response.getBody());
        verify(scheduleService, times(1)).findScheduleById(idSchedule);
    }

    @Test
    void findScheduleByIdInternalErrorTest(){
        Long idSchedule =1L;

        when(scheduleService.findScheduleById(idSchedule)).thenThrow(new RuntimeException("Server internal Error")) ;

        ResponseEntity<?> response = scheduleController.findScheduleById(idSchedule);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(scheduleService, times(1)).findScheduleById(idSchedule);
    }

    @Test
    void editScheduleSuccessTest (){
        Schedule schedule = new Schedule(1L, "8:00", "17:00", "MONDAY-FRIDAY");

        when(scheduleService.editSchedule(any(Schedule.class))).thenReturn(schedule);

        ResponseEntity<?> response = scheduleController.editSchedule(schedule);
        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(schedule, response.getBody());
        verify(scheduleService, times(1)).editSchedule(schedule);

    }

    @Test
    void editScheduleNotFoundTest (){
        Schedule schedule = new Schedule(1L, "8:00", "17:00", "MONDAY-FRIDAY");

        when(scheduleService.editSchedule(schedule)).thenThrow(new EntityNotFoundException("Schedule not found"));

        ResponseEntity<?> response = scheduleController.editSchedule(schedule);
        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Schedule not found", response.getBody());
        verify(scheduleService, times(1)).editSchedule(schedule);

    }
    @Test
    void editScheduleInternalErrorTest (){
        Schedule schedule = new Schedule(1L, "8:00", "17:00", "MONDAY-FRIDAY");

        when(scheduleService.editSchedule(schedule)).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity<?> response = scheduleController.editSchedule(schedule);
        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(scheduleService, times(1)).editSchedule(schedule);
    }

    @Test
    void deleteScheduleSuccessTest (){
        Long idSchedule =1L;

        doNothing().when(scheduleService).deleteScheduleById(idSchedule);

        ResponseEntity<?> response = scheduleController.deleteSchedule(idSchedule);
        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Delete Schedule susccessfully", response.getBody());
        verify(scheduleService, times(1)).deleteScheduleById(idSchedule);
    }

    @Test
    void deleteScheduleNotFoundTest (){
        Long idSchedule =1L;

        doThrow(new EntityNotFoundException("Schedule not found")).when(scheduleService).deleteScheduleById(idSchedule);

        ResponseEntity<?> response = scheduleController.deleteSchedule(idSchedule);
        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Schedule not found", response.getBody());
        verify(scheduleService, times(1)).deleteScheduleById(idSchedule);
    }
    @Test
    void deleteScheduleInternalErrorTest (){
        Long idSchedule =1L;

        doThrow(new RuntimeException("Server internal Error")).when(scheduleService).deleteScheduleById(idSchedule);

        ResponseEntity<?> response = scheduleController.deleteSchedule(idSchedule);
        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(scheduleService, times(1)).deleteScheduleById(idSchedule);
    }
}
