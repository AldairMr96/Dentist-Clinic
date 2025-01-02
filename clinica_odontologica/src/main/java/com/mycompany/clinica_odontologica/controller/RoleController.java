package com.mycompany.clinica_odontologica.controller;

import com.mycompany.clinica_odontologica.model.Role;
import com.mycompany.clinica_odontologica.service.IRoleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dental_clinic/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<Role> getRole (){
        List<Role> roles = roleService.getRole();
        return roles;
    }
    @GetMapping("/find")
    public ResponseEntity<?> findRoleById (@RequestParam Long idRole){
        try {
            Role role = roleService.findRoleById(idRole);
            return   ResponseEntity.ok(role);
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Role createRole (@RequestBody Role role){
        roleService.createRole(role);
        return role;
    }

    @PutMapping ("/edit")
    public ResponseEntity<?> editRole (Role role){
        try {
            roleService.editRole(role);
            return   ResponseEntity.ok("edit Role susccessfully" +
                    roleService.findRoleById(role.getIdRole()));
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRole (@RequestParam Long idRole){
        try {
            roleService.deleteRoleById(idRole);
            return   ResponseEntity.ok("Delete Role susccessfully");
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server internal Error");
        }
    }
}
