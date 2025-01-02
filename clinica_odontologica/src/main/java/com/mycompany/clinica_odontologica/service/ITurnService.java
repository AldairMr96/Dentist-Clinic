package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.Responsible;
import com.mycompany.clinica_odontologica.model.Turn;

import java.util.List;

public interface ITurnService {
    public  void createTurn (Turn turn);

    public List<Turn> getTurn();

    public Turn finTurnById (Long idTurn);

    public void deleteTurnById (Long idTurn);

    public Turn editTurn (Turn turn);

}
