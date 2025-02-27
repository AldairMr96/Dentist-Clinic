package com.mycompany.springsecurity.authenticationSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurityTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityTestApplication.class, args);
	}

/*	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IRoleRepository roleRepository;

	@Autowired
	private IPermissionRepository permissionRepository;

	@Autowired
	private IRolePermissionRepository rolePermissionRepository;

	@Bean
	CommandLineRunner init() {
		return args -> {
			// Crear permisos
			Permission createPermission = Permission.builder()
					.namePermission("CREATE")
					.build();
			Permission updatePermission = Permission.builder()
					.namePermission("UPDATE")
					.build();
			Permission deletePermission = Permission.builder()
					.namePermission("DELETE")
					.build();
			Permission readPermission = Permission.builder()
					.namePermission("READ")
					.build();

			// Guardar permisos en la base de datos
			permissionRepository.saveAll(List.of(createPermission, updatePermission, deletePermission, readPermission));

			// Crear roles
			Role roleDeveloper = Role.builder()
					.roleTypeEnum(RolesEnum.DEVELOPER)
					.build();

			Role roleManager = Role.builder()
					.roleTypeEnum(RolesEnum.MANAGER)
					.build();

			Role roleUser = Role.builder()
					.roleTypeEnum(RolesEnum.USER)
					.build();

			// Guardar roles en la base de datos
			roleRepository.saveAll(List.of(roleDeveloper, roleManager, roleUser));

			// Crear relaciones RolePermission
			rolePermissionRepository.saveAll(List.of(
					RolePermission.builder().role(roleDeveloper).permission(createPermission).build(),
					RolePermission.builder().role(roleDeveloper).permission(updatePermission).build(),
					RolePermission.builder().role(roleDeveloper).permission(deletePermission).build(),
					RolePermission.builder().role(roleDeveloper).permission(readPermission).build(),

					RolePermission.builder().role(roleManager).permission(createPermission).build(),
					RolePermission.builder().role(roleManager).permission(updatePermission).build(),
					RolePermission.builder().role(roleManager).permission(deletePermission).build(),
					RolePermission.builder().role(roleManager).permission(readPermission).build(),

					RolePermission.builder().role(roleUser).permission(createPermission).build(),
					RolePermission.builder().role(roleUser).permission(readPermission).build()
			));

			// Crear usuarios
			UserEntity userDeveloper = UserEntity.builder()
					.username("developer_user")
					.password("$2a$10$Sg/1dy4GWC1MEMb7kAqR4.og/GCpAabvu2QS1Z5rQJKXNrVtqBBk.")
					.isEnable(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.creadentialNoExpired(true)
					.roleType(roleDeveloper)
					.build();

			UserEntity userManager = UserEntity.builder()
					.username("manager_user")
					.password("$2a$10$Sg/1dy4GWC1MEMb7kAqR4.og/GCpAabvu2QS1Z5rQJKXNrVtqBBk.")
					.isEnable(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.creadentialNoExpired(true)
					.roleType(roleManager)
					.build();

			UserEntity user = UserEntity.builder()
					.username("normal_user")
					.password("$2a$10$Sg/1dy4GWC1MEMb7kAqR4.og/GCpAabvu2QS1Z5rQJKXNrVtqBBk.")
					.isEnable(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.creadentialNoExpired(true)
					.roleType(roleUser)
					.build();

			// Guardar usuarios en la base de datos
			userRepository.saveAll(List.of(userDeveloper, userManager, user));
		};
	}*/

}
