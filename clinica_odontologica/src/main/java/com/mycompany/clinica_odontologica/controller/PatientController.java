package com.mycompany.clinica_odontologica.controller;

import com.mycompany.clinica_odontologica.model.Dentist;
import com.mycompany.clinica_odontologica.model.Patient;
import com.mycompany.clinica_odontologica.service.IPatientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dental_clinic/patient")
public class PatientController {
    @Autowired
    private IPatientService patientService;

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<Patient> getPatient (){
        List<Patient> patients = patientService.getPatient();
        return patients;
    }
    @GetMapping("/find")
    public ResponseEntity<?> findPatientById (@RequestParam Long idPatient){
        try {
          Patient patient = patientService.findPatientById(idPatient);
            return  ResponseEntity.ok(patient);
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Patient createPatient (@RequestBody Patient patient){
        patientService.createPatient(patient);
        return patient;
    }

    @PutMapping ("/edit")
    public ResponseEntity<?> editPatient (@RequestBody  Patient patient){
        try {
            patientService.editPatient(patient);
            return  ResponseEntity.ok("edit patient susccessfully" +
                    patientService.findPatientById(patient.getIdPerson()));
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePatient (@RequestParam Long idPatient){
        try {
            patientService.deletePatientById(idPatient);
            return  ResponseEntity.ok("Delete dentist susccessfully");
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }

    @GetMapping("/medical-insurance")
    public ResponseEntity<?> getPatientWithMedicalInsure (@RequestParam String medicalInsurance){
        try {
            List <Patient> patients = patientService.getPatientWithMedicalInsure(medicalInsurance);
            return  ResponseEntity.ok("Patient whit "+ medicalInsurance + " : " + patients);
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }

    }


