package com.mycompany.clinica_odontologica.controller;

import com.mycompany.clinica_odontologica.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dental_clinic/auth")
public class UserController {
    @Autowired
    IUserService userService;


}
