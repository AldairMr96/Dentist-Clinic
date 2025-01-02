package com.mycompany.clinica_odontologica.controller;

import com.mycompany.clinica_odontologica.model.Dentist;
import com.mycompany.clinica_odontologica.model.Responsible;
import com.mycompany.clinica_odontologica.service.IResponsibleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dental_clinic/responsible")
public class ResponsibleController {
    @Autowired
    private IResponsibleService responsibleService;

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<Responsible> getResponsible (){
        List<Responsible> responsibles = responsibleService.getResponsibles();
        return responsibles;
    }
    @GetMapping("/find")
    public ResponseEntity<?> findResponsibleById (@RequestParam Long idResponsible){
        try {
           Responsible responsibleFound =responsibleService.finResponsibletById(idResponsible);
            return  ResponseEntity.ok(responsibleFound);
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Responsible createResponsible (@RequestBody Responsible responsible){
        responsibleService.createResponsible(responsible);
        return  responsible;
    }
    @PutMapping("/edit")
    public ResponseEntity<?> editResponsible (Responsible responsible) {
        try{
           responsibleService.editResponsible(responsible);
            return  ResponseEntity.ok("edit responsible susccessfully" + responsibleService.finResponsibletById(responsible.getIdPerson()));
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }

    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteResponsible(  @RequestParam Long idResponsible) {
        try {
            responsibleService.deleteResponsibleById(idResponsible);
            return ResponseEntity.ok("Delete responsible susccessfully");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }

}
