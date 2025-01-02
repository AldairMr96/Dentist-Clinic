package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.Responsible;
import com.mycompany.clinica_odontologica.repository.IResponsibleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ResponsibleService implements IResponsibleService{

    @Autowired
    private IResponsibleRepository responsibleRepository;
    @Override
    public void createResponsible(Responsible responsible) {
        responsibleRepository.save(responsible);
    }

    @Override
    public List<Responsible> getResponsibles() {
        List<Responsible> responsibles =responsibleRepository.findAll();
        return responsibles;
    }

    @Override
    public Responsible finResponsibletById(Long idResponsible) {
        Responsible responsibleResult= responsibleRepository.findById(idResponsible)
                .orElseThrow(()-> new EntityNotFoundException("Responsible not found"));
        return responsibleResult;
    }

    @Override
    public void deleteResponsibleById(Long idResponsible) {
        if (!responsibleRepository.existsById(idResponsible)){
            throw new EntityNotFoundException("Responsible not found");
        }
        responsibleRepository.deleteById(idResponsible);
    }

    @Override
    public Responsible editResponsible(Responsible responsible) {
        Responsible responsibleFound = this.finResponsibletById(responsible.getIdPerson());

        responsibleFound.setDni(responsible.getDni());
        responsibleFound.setName(responsible.getLastname());
        responsibleFound.setNumberPhone(responsible.getNumberPhone());
        responsibleFound.setAddress(responsible.getAddress());
        responsibleFound.setDateOfBirth(responsible.getDateOfBirth());
        responsibleFound.setRelationshipType(responsible.getRelationshipType());
        responsibleFound.setRelationshipPatient(responsible.getRelationshipPatient());

        this.createResponsible(responsibleFound);

        return responsibleFound;
    }
}
