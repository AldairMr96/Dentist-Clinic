package com.mycompany.clinica_odontologica.controller;

import com.mycompany.clinica_odontologica.model.Dentist;
import com.mycompany.clinica_odontologica.service.DentistService;
import com.mycompany.clinica_odontologica.service.IDentistService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dental_clinic/dentist")
public class DentistController {
    @Autowired
    private IDentistService dentistService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Dentist createDentist (@RequestBody Dentist dentist){
        dentistService.createDentist(dentist);
        return dentist;
    }
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<Dentist> getDentists (){
        List<Dentist> dentists = dentistService.getDentists();
        return dentists;
    }
    @GetMapping("/find")
    public ResponseEntity<?> findDentistById (@RequestParam Long idDentist){
        try {
            Dentist dentist =dentistService.findDentistById(idDentist);
            return  ResponseEntity.ok(dentist);
        }catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editDentist(Dentist dentist) {
        try{
             dentistService.editDentist(dentist);
            return  ResponseEntity.ok("edit dentist susccessfully" + dentistService.findDentistById(dentist.getIdPerson()));
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteDentist(  @RequestParam Long idDentist) {
        try{
            dentistService.deleteDentistById(idDentist);
            return  ResponseEntity.ok("Delete dentist susccessfully");
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }

    }






}
