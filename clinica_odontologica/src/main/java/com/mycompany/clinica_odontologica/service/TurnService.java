package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.Responsible;
import com.mycompany.clinica_odontologica.model.Turn;
import com.mycompany.clinica_odontologica.repository.IDentistRepository;
import com.mycompany.clinica_odontologica.repository.ITurnRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TurnService implements ITurnService {
    @Autowired
    private ITurnRepository turnRepository;
    @Autowired
    private IDentistRepository dentistRepository;

    @Override
    public void createTurn(Turn turn) {
        turnRepository.save(turn);
    }

    @Override
    public List<Turn> getTurn() {
        List<Turn> turns = turnRepository.findAll();
        return turns;
    }

    @Override
    public Turn finTurnById(Long idTurn) {
        Turn turnFound = turnRepository.findById(idTurn)
                .orElseThrow(()-> new EntityNotFoundException("Turn not found"));
        return turnFound;
    }

    @Override
    public void deleteTurnById(Long idTurn) {
        if(!turnRepository.existsById(idTurn)){
            throw new EntityNotFoundException("Turn not found");
        }
        turnRepository.deleteById(idTurn);
    }

    @Override
    public Turn editTurn(Turn turn) {
        Turn turnFound = this.finTurnById(turn.getIdTurn());
        turnFound.setDateTurn(turn.getDateTurn());
        turnFound.setShiftTime(turn.getShiftTime());
        turnFound.setDisease(turn.getDisease());
        turnFound.setDentist(turn.getDentist());
        turnFound.setPatient(turn.getPatient());

        this.createTurn(turnFound);
        return turnFound;
    }

    @Override
    public List<Turn> turnsDentistsPerDay(Long idDentist, LocalDate date) {
           if( !dentistRepository.existsById(idDentist)){
               throw  new EntityNotFoundException("Denstist not found");
           }
            List<Turn> turnsDentistsPerDay = turnRepository.findByDentist_IdAndDateTurn(idDentist, date);

        return turnsDentistsPerDay;
    }
}
