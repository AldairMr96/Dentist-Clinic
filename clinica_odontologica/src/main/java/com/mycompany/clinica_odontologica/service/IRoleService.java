package com.mycompany.clinica_odontologica.service;


import com.mycompany.clinica_odontologica.model.Role;

import java.util.List;

public interface IRoleService {
    public  void createRole (Role role);

    public List<Role> getRole();

    public Role findRoleById (Long idrole);

    public void deleteRoleById (Long idrole);

    public Role editRole (Role role);
    
}
