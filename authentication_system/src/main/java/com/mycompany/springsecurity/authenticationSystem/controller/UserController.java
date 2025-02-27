package com.mycompany.springsecurity.authenticationSystem.controller;

import com.mycompany.springsecurity.authenticationSystem.dto.AuthCreateUserRequest;
import com.mycompany.springsecurity.authenticationSystem.dto.AuthLoginRequest;
import com.mycompany.springsecurity.authenticationSystem.model.UserEntity;
import com.mycompany.springsecurity.authenticationSystem.repository.IUserRepository;
import com.mycompany.springsecurity.authenticationSystem.service.IUserService;
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
@RequestMapping ("/authentication")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private UserDetailsService userDetailService;
    @Autowired
    private IUserService userService;
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List <UserEntity> getUsers(){
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
    public  ResponseEntity<?> createUser(@RequestBody @Valid AuthCreateUserRequest userRequest){
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
    public ResponseEntity<?> loginUser (@RequestBody @Valid AuthLoginRequest userRequest){
        try {
            return new ResponseEntity<>(this.userService.loginUser(userRequest), HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }

    }





}
