package com.mycompany.clinica_odontologica.service;


import com.mycompany.clinica_odontologica.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface IUserService {
    public  void createUser (String username, String password);

    public List<User> getUser();

    public User finUserById (Long idUser);

    public void deleteUserById (Long idUser);

    public User editUser (User user);

    ResponseEntity<String> singUp (Map<String, String> requestMap);
}
