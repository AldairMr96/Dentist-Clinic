package com.mycompany.clinica_odontologica.service.test;

import com.mycompany.clinica_odontologica.model.Schedule;
import com.mycompany.clinica_odontologica.repository.IScheduleRepository;
import com.mycompany.clinica_odontologica.service.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;
public class ScheduleServiceTest {

    @Mock
    private IScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;
    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void createScheduleTest (){
        Schedule schedule = new Schedule();
        schedule.setIdSchedule(1L);
        schedule.setStarTime("8:00");
        schedule.setTimeOver("17:00");
        schedule.setWorkingDays("Monday - Friday");
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        Schedule result = scheduleService.createSchedule(schedule);

        assertEquals(schedule.getIdSchedule(), result.getIdSchedule());
        assertEquals(schedule.getStarTime(), result.getStarTime());
        assertEquals(schedule.getTimeOver(), result.getTimeOver());
        assertEquals(schedule.getWorkingDays(), result.getWorkingDays());
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }

    @Test
    void getSchedulesEmptyTest (){
        when(scheduleRepository.findAll()).thenReturn(Collections.emptyList());

        List<Schedule> result = scheduleService.getSchedule();

        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        verify(scheduleRepository, times(1)).findAll();
    }

    @Test
    void getSchedulesTest (){
        Schedule schedule = new Schedule();
        schedule.setIdSchedule(1L);
        schedule.setStarTime("8:00");
        schedule.setTimeOver("17:00");
        schedule.setWorkingDays("Monday - Friday");
        List<Schedule> schedules = List.of(schedule);

        when(scheduleRepository.findAll()).thenReturn(schedules);
        List<Schedule> result = scheduleService.getSchedule();

        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        verify(scheduleRepository, times(1)).findAll();
    }

    @Test
    void findScheduleByIdNotFoundTest (){
        Long idSchedule = 1L;

        when(scheduleRepository.findById(idSchedule)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                scheduleService.findScheduleById(idSchedule));
        verify(scheduleRepository, times(1)).findById(idSchedule);
    }

    @Test
    void findScheduleByIdTest (){
        Long idSchedule = 1L;
        Schedule schedule = new Schedule();
        schedule.setIdSchedule(idSchedule);
        schedule.setStarTime("8:00");
        schedule.setTimeOver("17:00");
        schedule.setWorkingDays("Monday - Friday");
        when(scheduleRepository.findById(idSchedule)).thenReturn(Optional.of(schedule));

        Schedule result = scheduleService.findScheduleById(idSchedule);

        assertNotNull(result);
        assertEquals(schedule.getIdSchedule(), result.getIdSchedule());
        assertEquals(schedule.getStarTime(), result.getStarTime());
        assertEquals(schedule.getTimeOver(), result.getTimeOver());
        assertEquals(schedule.getWorkingDays(), result.getWorkingDays());
        verify(scheduleRepository, times(1)).findById(idSchedule);

    }

    @Test
    void deleteByIdNotFoundTest (){
        Long idSchedule = 1L;
        when(scheduleRepository.existsById(idSchedule)).thenReturn(false);
        doThrow(new EntityNotFoundException("Schedule not found")).when(scheduleRepository).deleteById(idSchedule);

        assertThrows(EntityNotFoundException.class, () ->
                scheduleService.deleteScheduleById(idSchedule));
        verify(scheduleRepository, times(1)).existsById(idSchedule);
        verify(scheduleRepository, never()).deleteById(idSchedule);
    }

    @Test
    void deleteByIdTest (){
        Long idSchedule = 1L;
        when(scheduleRepository.existsById(idSchedule)).thenReturn(true);
        doNothing().when(scheduleRepository).deleteById(idSchedule);

        scheduleService.deleteScheduleById(idSchedule);
        verify(scheduleRepository, times(1)).existsById(idSchedule);
        verify(scheduleRepository, times(1)).deleteById(idSchedule);
    }

    @Test
    void editScheduleNotFoundTest (){
        Long idSchedule  = 1L;

        Schedule schedule = new Schedule();
        schedule.setIdSchedule(idSchedule);

        when(scheduleRepository.findById(idSchedule)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                ()-> scheduleService.editSchedule(schedule));

        verify(scheduleRepository, times(1)).findById(idSchedule);
        verify(scheduleRepository, never()).save(schedule);

    }
    @Test
    void editScheduleSuccessTest (){

        Long idSchedule = 1L;
        Schedule schedule = new Schedule();
        schedule.setIdSchedule(idSchedule);
        schedule.setStarTime("8:00");
        schedule.setTimeOver("17:00");
        schedule.setWorkingDays("Monday - Friday");

        Schedule updateSchedule = new Schedule();
        updateSchedule.setIdSchedule(idSchedule);
        updateSchedule.setStarTime("8:00");
        updateSchedule.setTimeOver("17:00");
        updateSchedule.setWorkingDays("Monday - Saturday");

        when(scheduleRepository.findById(idSchedule)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(schedule)).thenReturn(updateSchedule);

        Schedule result = scheduleService.editSchedule(updateSchedule);

        assertNotNull(result);
        assertEquals(updateSchedule.getIdSchedule(), result.getIdSchedule());
        assertEquals(updateSchedule.getStarTime(), result.getStarTime());
        assertEquals(updateSchedule.getTimeOver(), result.getTimeOver());
        assertEquals(updateSchedule.getWorkingDays(), result.getWorkingDays());
        verify(scheduleRepository, times(1)).findById(idSchedule);
        verify(scheduleRepository, times(1)).save(schedule);
    }
}
