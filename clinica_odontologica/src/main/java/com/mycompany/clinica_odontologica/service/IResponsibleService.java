package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.Responsible;

import java.util.List;

public interface IResponsibleService {

    public  void createResponsible (Responsible responsible);

    public List<Responsible> getResponsibles();

    public Responsible finResponsibletById (Long idResponsible);

    public void deleteResponsibleById (Long idResponsible);

    public Responsible editResponsible (Responsible responsible);

}
