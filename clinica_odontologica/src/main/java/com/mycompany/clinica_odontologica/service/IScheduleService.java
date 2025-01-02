package com.mycompany.clinica_odontologica.service;


import com.mycompany.clinica_odontologica.model.Schedule;

import java.util.List;

public interface IScheduleService {
    public  void createSchedule (Schedule schedule);

    public List<Schedule> getSchedule();

    public Schedule findScheduleById (Long idSchedule);

    public void deleteScheduleById (Long idSchedule);

    public Schedule editSchedule (Schedule schedule);
}
