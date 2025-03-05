package com.mycompany.clinica_odontologica.service;


import com.mycompany.clinica_odontologica.dto.AuthCreateUser;
import com.mycompany.clinica_odontologica.dto.AuthLogin;
import com.mycompany.clinica_odontologica.dto.AuthResponse;
import com.mycompany.clinica_odontologica.model.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;
import java.util.Map;

public interface IUserService {
    public  void createUser (AuthCreateUser authCreateUser);

    public List<UserEntity> getUser();

    public UserEntity finUserById (Long idUser);

    public void deleteUserById (Long idUser);

    public UserEntity editUser (UserEntity userEntity);

    public AuthResponse loginUser(AuthLogin authLogin, AuthenticationManager authenticationManager);


}
