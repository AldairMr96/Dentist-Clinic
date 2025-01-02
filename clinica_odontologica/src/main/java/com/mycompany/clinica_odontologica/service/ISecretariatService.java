package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.Secretariat;

import java.util.List;

public interface ISecretariatService {
    public  void createSecretariat(Secretariat secretariat);

    public List<Secretariat> getSecretariats();

    public Secretariat findSecretariatById (Long idSecretariat);

    public void deleteSecretariatById (Long idSecretariat);

    public Secretariat editSecretariat(Secretariat secretariat);
}
