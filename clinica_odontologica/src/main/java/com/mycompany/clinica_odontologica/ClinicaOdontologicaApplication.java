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

import java.util.List;

@SpringBootApplication
public class ClinicaOdontologicaApplication {

		public static void main(String[] args) {
			SpringApplication.run(ClinicaOdontologicaApplication.class, args);}
		/*@Autowired
		private IPermissionsEntityRepository permissionsRepository;
		@Autowired
		private IRoleRepository roleRepository;
		@Autowired
		private IRolePermissionsRepository rolePermissionsRepository;
		@Autowired
		private IUserRepository userRepository;


		@Bean
		CommandLineRunner init () {
			return args -> {
				//Create permissions
				PermissionsEntity createPermission = PermissionsEntity.builder()
						.namePermission("CREATE")
						.build();
				PermissionsEntity updatePermission = PermissionsEntity.builder()
						.namePermission("UPDATE")
						.build();
				PermissionsEntity deletePermission = PermissionsEntity.builder()
						.namePermission("DELETE")
						.build();
				PermissionsEntity readPermission = PermissionsEntity.builder()
						.namePermission("READ")
						.build();
				//Save permission in the DB
				permissionsRepository.saveAll(List.of(createPermission, updatePermission, deletePermission, readPermission));

				//Create Roles
				Role roleDentist = Role.builder().
						roleTypeEnum(RoleEnum.DENTIST)
						.build();
				Role rolePatient = Role.builder().
						roleTypeEnum(RoleEnum.PATIENT)
						.build();
				Role roleSecretariat = Role.builder().
						roleTypeEnum(RoleEnum.SECRETARIAT)
						.build();
				//Save Roles in the DB
				roleRepository.saveAll(List.of(roleDentist, roleSecretariat, rolePatient));

				//Create relationships role-permissions

				rolePermissionsRepository.saveAll(List.of(
						//Role-Permission Secretariat
						RolePermissions.builder().role(roleSecretariat).permissionEntity(createPermission).build(),
						RolePermissions.builder().role(roleSecretariat).permissionEntity(updatePermission).build(),
						RolePermissions.builder().role(roleSecretariat).permissionEntity(deletePermission).build(),
						RolePermissions.builder().role(roleSecretariat).permissionEntity(readPermission).build(),
						//Role-Permission Dentist
						RolePermissions.builder().role(roleDentist).permissionEntity(createPermission).build(),
						RolePermissions.builder().role(roleDentist).permissionEntity(updatePermission).build(),
						RolePermissions.builder().role(roleDentist).permissionEntity(deletePermission).build(),
						RolePermissions.builder().role(roleDentist).permissionEntity(readPermission).build(),
						//Role-Permission Patient
						RolePermissions.builder().role(rolePatient).permissionEntity(createPermission).build(),
						RolePermissions.builder().role(rolePatient).permissionEntity(readPermission).build(),
						RolePermissions.builder().role(rolePatient).permissionEntity(updatePermission).build()

				));

				//Create Users
				UserEntity userDentist = UserEntity.builder()
						.username("dentist_user")
						.password("$2a$10$KUQZB7DBHb2EupnuZkkv1OUfL1rTE/qSyd3i942Bi6QFb1ngZbzZi")
						.isEnable(true)
						.accountNoExpired(true)
						.accountNoLocked(true)
						.creadentialNoExpired(true)
						.roleType(roleDentist)
						.build();

				UserEntity userSecretariat = UserEntity.builder()
						.username("secretariat_user")
						.password("$2a$10$KUQZB7DBHb2EupnuZkkv1OUfL1rTE/qSyd3i942Bi6QFb1ngZbzZi")
						.isEnable(true)
						.accountNoExpired(true)
						.accountNoLocked(true)
						.creadentialNoExpired(true)
						.roleType(roleSecretariat)
						.build();

				UserEntity userPatient= UserEntity.builder()
						.username("patient_user")
						.password("$2a$10$KUQZB7DBHb2EupnuZkkv1OUfL1rTE/qSyd3i942Bi6QFb1ngZbzZi")
						.isEnable(true)
						.accountNoExpired(true)
						.accountNoLocked(true)
						.creadentialNoExpired(true)
						.roleType(rolePatient)
						.build();

				//Save Users in the DB
				userRepository.saveAll(List.of(userSecretariat, userPatient, userDentist));
			};
		}*/


}
