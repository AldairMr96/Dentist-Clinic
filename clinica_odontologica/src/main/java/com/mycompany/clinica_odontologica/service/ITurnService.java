package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.Responsible;
import com.mycompany.clinica_odontologica.model.Turn;

import java.time.LocalDate;
import java.util.List;

public interface ITurnService {
    public  Turn createTurn (Turn turn);

    public List<Turn> getTurn();

    public Turn finTurnById (Long idTurn);

    public void deleteTurnById (Long idTurn);

    public Turn editTurn (Turn turn);

    public List<Turn> turnsDentistsPerDay(Long idDentist, LocalDate date);

}
