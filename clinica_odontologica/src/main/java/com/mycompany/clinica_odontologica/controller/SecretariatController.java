package com.mycompany.clinica_odontologica.controller;

import com.mycompany.clinica_odontologica.model.Dentist;
import com.mycompany.clinica_odontologica.model.Secretariat;
import com.mycompany.clinica_odontologica.service.ISecretariatService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dental_clinic/secretariat")
public class SecretariatController {
    @Autowired
    private ISecretariatService secretariatService;
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<Secretariat> getSecretariats (){
        List<Secretariat> secretariats = secretariatService.getSecretariats();
        return secretariats;
    }
    @GetMapping("/find")
    public ResponseEntity<?> findSecretariatById (@RequestParam Long idSecretariat){
        try {
            Secretariat secretariat =secretariatService.findSecretariatById(idSecretariat);
            return  ResponseEntity.ok(secretariat);
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Secretariat createSecretariat (@RequestBody Secretariat secretariat){
        secretariatService.createSecretariat(secretariat);
        return secretariat;
    }
    @PutMapping("/edit")
    public ResponseEntity<?> editSecretariat(Secretariat secretariat) {
        try{
            secretariatService.editSecretariat(secretariat);
            return  ResponseEntity.ok("edit Secretariat susccessfully" +
                    secretariatService.findSecretariatById(secretariat.getIdPerson()));
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSecretariat(  @RequestParam Long idSecretariat) {
        try{
            secretariatService.deleteSecretariatById(idSecretariat);
            return  ResponseEntity.ok("Delete Secretariat susccessfully");
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }

    }
}
