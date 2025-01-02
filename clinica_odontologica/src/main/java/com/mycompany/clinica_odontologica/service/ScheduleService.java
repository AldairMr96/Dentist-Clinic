package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.Schedule;
import com.mycompany.clinica_odontologica.repository.IScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService implements IScheduleService {
    @Autowired
    private IScheduleRepository scheduleRepository;
    @Override
    public void createSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getSchedule() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules;
    }

    @Override
    public Schedule findScheduleById(Long idSchedule) {
        Schedule scheduleFound = scheduleRepository.findById(idSchedule)
                .orElseThrow(()-> new EntityNotFoundException("Schedule not found"));

        return scheduleFound;
    }

    @Override
    public void deleteScheduleById(Long idSchedule) {
        if(!scheduleRepository.existsById(idSchedule)){
            throw  new EntityNotFoundException("Schedule not found");
        }
        scheduleRepository.deleteById(idSchedule);
    }

    @Override
    public Schedule editSchedule(Schedule schedule) {
        Schedule scheduleFound = this.findScheduleById(schedule.getIdSchedule());
        scheduleFound.setStarTime(schedule.getStarTime());
        scheduleFound.setTimeOver(schedule.getTimeOver());
        scheduleFound.setWorkingDays(schedule.getWorkingDays());

        this.createSchedule(scheduleFound);
        return scheduleFound;
    }
}
