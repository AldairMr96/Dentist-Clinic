package com.mycompany.clinica_odontologica;

import com.mycompany.clinica_odontologica.model.*;
import com.mycompany.clinica_odontologica.repository.IPermissionsEntityRepository;
import com.mycompany.clinica_odontologica.repository.IRolePermissionsRepository;
import com.mycompany.clinica_odontologica.repository.IRoleRepository;
import com.mycompany.clinica_odontologica.repository.IUserRepository;
import com.mycompany.clinica_odontologica.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootApplication
public class ClinicaOdontologicaApplication {

		public static void main(String[] args) {
			SpringApplication.run(ClinicaOdontologicaApplication.class, args);
		}

}
