package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.User;
import com.mycompany.clinica_odontologica.repository.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public void createUser(String username, String password) {
      User userCrypt = new User();

    }

    @Override
    public List<User> getUser() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public User finUserById(Long idUser) {
        User userFound = userRepository.findById(idUser)
                .orElseThrow(()-> new EntityNotFoundException("User not found")) ;
        return userFound;
    }

    @Override
    public void deleteUserById(Long idUser) {
        if(!userRepository.existsById(idUser)) {
            throw  new EntityNotFoundException("User not found");
        }
         userRepository.deleteById(idUser);

    }

    @Override
    public User editUser(User user) {

        return null;
    }

    @Override
    public ResponseEntity<String> singUp(Map<String, String> requestMap) {
        return null;
    }
}
