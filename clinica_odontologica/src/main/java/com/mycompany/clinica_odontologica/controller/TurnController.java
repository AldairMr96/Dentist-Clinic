package com.mycompany.clinica_odontologica.controller;


import com.mycompany.clinica_odontologica.model.Turn;
import com.mycompany.clinica_odontologica.service.ITurnService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dental_clinic/turn")
public class TurnController {
    @Autowired
    private ITurnService turnService;
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<Turn> getTurn (){
        List<Turn> turns = turnService.getTurn();
        return turns;
    }
    @GetMapping("/find")
    public ResponseEntity<?> findTurnById (@RequestParam Long idTurn){
        try {
            Turn turn = turnService.finTurnById(idTurn);
            return  ResponseEntity.ok(turn);
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Turn createTurn (@RequestBody Turn turn){
        turnService.createTurn(turn);
        return turn;
    }

    @PutMapping ("/edit")
    public ResponseEntity<?> editTurn (Turn turn){
        try {
            turnService.editTurn(turn);
            return  ResponseEntity.ok("edit turn susccessfully" +
                    turnService.finTurnById(turn.getIdTurn()));
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTurn (@RequestParam Long idTurn){
        try {
           turnService.deleteTurnById(idTurn);
            return  ResponseEntity.ok("Delete turn susccessfully");
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }

    @GetMapping("/get-turns-for-day")
    public ResponseEntity<?> findTurnDentistPerDay (@RequestParam Long idDentist,
                                                    @RequestParam("date")
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){

       try {
           List<Turn> turns = turnService.turnsDentistsPerDay(idDentist, date);
           return ResponseEntity.ok(turns);
       }catch (EntityNotFoundException ex){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
       }catch (Exception ex){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
       }

    }
}
