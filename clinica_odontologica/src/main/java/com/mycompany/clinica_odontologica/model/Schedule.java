package com.mycompany.clinica_odontologica.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name= "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "schedules_seq", allocationSize = 1)
    private Long idSchedule;
    @Column(name = "star_time", nullable = false)
    private String starTime;
    @Column(name = "time_over", nullable = false)
    private String timeOver;
    @Column(name = "working_days", nullable = false)
    private String workingDays;




}
