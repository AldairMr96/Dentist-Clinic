package com.mycompany.clinica_odontologica.controller;

import com.mycompany.clinica_odontologica.model.Dentist;
import com.mycompany.clinica_odontologica.model.Schedule;
import com.mycompany.clinica_odontologica.service.IScheduleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dental_clinic/schedule")
public class ScheduleController {
    @Autowired
    private IScheduleService scheduleService;

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<Schedule> getSchedule (){
        List<Schedule> schedules = scheduleService.getSchedule();
        return schedules;
    }
    @GetMapping("/find")
    public ResponseEntity<?> findScheduleById (@RequestParam Long idSchedule){
        try {
            Schedule schedule =scheduleService.findScheduleById(idSchedule);
            return  ResponseEntity.ok(schedule);
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Schedule createSchedule (@RequestBody Schedule schedule){
        System.out.println("id schedule is : " + schedule.getIdSchedule());
         return scheduleService.createSchedule(schedule);
    }
    @PutMapping("/edit")
    public ResponseEntity<?> editSchedule(@RequestBody Schedule schedule) {
        try{
            scheduleService.editSchedule(schedule);
            return  ResponseEntity.ok("edit Schedule susccessfully" +
                   scheduleService.findScheduleById(schedule.getIdSchedule()));
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSchedule(  @RequestParam Long idSchedule) {
        try{
            scheduleService.deleteScheduleById(idSchedule);
            return  ResponseEntity.ok("Delete Schedule susccessfully");
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }

    }
}
