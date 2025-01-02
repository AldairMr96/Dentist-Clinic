package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.model.Role;
import com.mycompany.clinica_odontologica.repository.IRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;
    @Override
    public void createRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public List<Role> getRole() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }

    @Override
    public Role findRoleById(Long idrole) {
        Role roleFound = roleRepository.findById(idrole)
                .orElseThrow(()-> new EntityNotFoundException("Role not found"));

        return roleFound;
    }

    @Override
    public void deleteRoleById(Long idrole) {
        if (!roleRepository.existsById(idrole)) {
            throw  new EntityNotFoundException("Role not found");
        }
        roleRepository.deleteById(idrole);
    }

    @Override
    public Role editRole(Role role) {
        Role roleFound = this.findRoleById(role.getIdRole());

        roleFound.setRoleType(role.getRoleType());
        roleFound.setUsers(role.getUsers());
        return null;
    }
}
