package com.mycompany.clinica_odontologica.controller;

import com.mycompany.clinica_odontologica.dto.AuthCreateUser;
import com.mycompany.clinica_odontologica.dto.AuthLogin;
import com.mycompany.clinica_odontologica.dto.AuthResponse;
import com.mycompany.clinica_odontologica.model.UserEntity;
import com.mycompany.clinica_odontologica.repository.IRoleRepository;
import com.mycompany.clinica_odontologica.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dental_clinic/auth")
//@CrossOrigin(origins = "*")
public class UserController {

@Autowired
private IUserService userService;
@Autowired
private IRoleRepository roleRepository;
@Autowired
private AuthenticationManager authenticationManager;

private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<UserEntity> getUsers(){
        List <UserEntity> userEntities =  userService.getUser();
        return userEntities;
    }
    @GetMapping("/find")
    public ResponseEntity<?> findUserById (@RequestParam Long idUserEntity){
        try {
            UserEntity userEntity =userService.finUserById(idUserEntity);
            return  ResponseEntity.ok(userEntity);
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }

    }

    @PostMapping("/sigin")
    @ResponseStatus(HttpStatus.CREATED)
    public  ResponseEntity<?> createUser(@RequestBody @Valid AuthCreateUser userRequest){

        return  ResponseEntity.ok(userService.createUser(userRequest));
    }
    @PutMapping("/edit")
    public ResponseEntity<?> editUser(@RequestBody UserEntity userEntity) {
        try{
            userService.editUser(userEntity);
            return  ResponseEntity.ok("edit user susccessfully "
            );
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }

    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam String  username) {
        try {
            userService.deleteUserById(username);
            return ResponseEntity.ok("Delete user susccessfully");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser (@RequestBody @Valid AuthLogin userRequest){
        logger.info("Login request received");
        try {
            AuthResponse response = userService.loginUser(userRequest, authenticationManager);
            System.out.println("Response: " + response);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }

    }

}
