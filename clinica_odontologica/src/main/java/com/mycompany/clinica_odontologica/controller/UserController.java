package com.mycompany.clinica_odontologica.controller;

import com.mycompany.clinica_odontologica.dto.AuthCreateUser;
import com.mycompany.clinica_odontologica.dto.AuthLogin;
import com.mycompany.clinica_odontologica.model.UserEntity;
import com.mycompany.clinica_odontologica.repository.IRoleRepository;
import com.mycompany.clinica_odontologica.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserController {

@Autowired
private IUserService userService;
@Autowired
private IRoleRepository roleRepository;
@Autowired
private UserDetailsService userDetailsService;

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

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.CREATED)
    public  ResponseEntity<?> createUser(@RequestBody @Valid AuthCreateUser userRequest){
        userService.createUser(userRequest);
        return  ResponseEntity.ok("Create user susccessfully ");
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
    public ResponseEntity<?> deleteUser(@RequestParam Long idUserEntity) {
        try {
            userService.deleteUserById(idUserEntity);
            return ResponseEntity.ok("Delete user susccessfully");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }
    @PostMapping("/log-in")
    public ResponseEntity<?> loginUser (@RequestBody @Valid AuthLogin userRequest){
        try {
            return new ResponseEntity<>(this.userService.loginUser(userRequest), HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }

    }

}
