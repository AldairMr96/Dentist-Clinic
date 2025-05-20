package com.mycompany.clinica_odontologica.service.test;

import com.mycompany.clinica_odontologica.dto.AuthCreateRoleRequest;
import com.mycompany.clinica_odontologica.dto.AuthCreateUser;
import com.mycompany.clinica_odontologica.dto.AuthLogin;
import com.mycompany.clinica_odontologica.dto.AuthResponse;
import com.mycompany.clinica_odontologica.model.*;
import com.mycompany.clinica_odontologica.repository.IRoleRepository;
import com.mycompany.clinica_odontologica.repository.IUserRepository;
import com.mycompany.clinica_odontologica.service.IUserService;
import com.mycompany.clinica_odontologica.service.UserService;
import com.mycompany.clinica_odontologica.util.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class UserServiceTest {
    @Mock
    private IUserRepository userRepository;

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp(){
        openMocks(this);
    }

    @Test
    void loadLoadUsernameUserExistsTest(){
        //Arrange
        String username = "test_user";
        String password = "password_test";
        Role mockRole = new Role();
        mockRole.setRoleTypeEnum(RoleEnum.SECRETARIAT);
        PermissionsEntity permissions = new PermissionsEntity(1L, "READ", null);
        RolePermissions rolePermissions = new RolePermissions(1L, mockRole, permissions);
        mockRole.setRolePermissions(Set.of(rolePermissions));

        UserEntity mockUser = new UserEntity();
        mockUser.setUsername(username);
        mockUser.setPassword(password);
        mockUser.setIsEnable(true);
        mockUser.setAccountNoExpired(true);
        mockUser.setCreadentialNoExpired(true);
        mockUser.setAccountNoLocked(true);
        mockUser.setRoleType(mockRole);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_".concat(mockUser.getRoleType().getRoleTypeEnum().name())));
        mockUser.getRoleType().getRolePermissions().forEach(rolePermissionsFinding -> authorities
                        .add(new SimpleGrantedAuthority(rolePermissions.getPermissionEntity().getNamePermission())));
        User userDetail = new User(
                mockUser.getUsername(),
                mockUser.getPassword(),
                mockUser.getIsEnable(),
                mockUser.getAccountNoExpired(),
                mockUser.getCreadentialNoExpired(),
                mockUser.getAccountNoLocked(),
                authorities
        );

        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(mockUser));

        UserDetails result = userService.loadUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).findUserEntityByUsername(username);
    }


    @Test
    void loadUserByUsernameUserNotFoundTest(){
        String username = "test_user";
        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class,()-> userService.loadUserByUsername(username));
        verify(userRepository, times(1)).findUserEntityByUsername(username);
    }

    @Test
    void createUserSuccessTest(){
        String username = "test_user";
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";

        Role mockRole = new Role();
        mockRole.setRoleTypeEnum(RoleEnum.SECRETARIAT);
        PermissionsEntity permissions = new PermissionsEntity(1L, "READ", null);
        RolePermissions rolePermissions = new RolePermissions(1L, mockRole, permissions);
        mockRole.setRolePermissions(Set.of(rolePermissions));

        UserEntity mockUser = new UserEntity();
        mockUser.setUsername(username);
        mockUser.setPassword(encodedPassword);
        mockUser.setIsEnable(true);
        mockUser.setAccountNoExpired(true);
        mockUser.setCreadentialNoExpired(true);
        mockUser.setAccountNoLocked(true);
        mockUser.setRoleType(mockRole);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_".concat(mockUser.getRoleType().getRoleTypeEnum().name())));
        mockUser.getRoleType().getRolePermissions().forEach(rolePermissionsFinding -> authorities
                .add(new SimpleGrantedAuthority(rolePermissions.getPermissionEntity().getNamePermission())));
        User userDetail = new User(
                mockUser.getUsername(),
                mockUser.getPassword(),
                mockUser.getIsEnable(),
                mockUser.getAccountNoExpired(),
                mockUser.getCreadentialNoExpired(),
                mockUser.getAccountNoLocked(),
                authorities
        );
        AuthCreateUser request = new AuthCreateUser(username, rawPassword, new AuthCreateRoleRequest(mockRole.getRoleTypeEnum().name()));
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(roleRepository.findByRoleTypeEnum(mockRole.getRoleTypeEnum())).thenReturn(Optional.of(mockRole));
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(mockUser));
        when(jwtUtils.createToken(anyString(), anyString())).thenReturn("Token");

        AuthResponse authResponse = userService.createUser(request);

        verify(userRepository, times(1)).save(any(UserEntity.class));
        assertEquals(username, authResponse.username());
        assertEquals("User created successfully", authResponse.message());
        assertEquals("Token", authResponse.jwt());
        assertTrue(authResponse.status());
    }

    @Test
    void createUserExistedTest (){
        String username ="existingUser";
        RoleEnum roleEnum = RoleEnum.SECRETARIAT;
        AuthCreateUser request = new AuthCreateUser(username, "password", new AuthCreateRoleRequest(roleEnum.name()));
        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, ()-> userService.createUser(request));
        verify(userRepository, never()).save(any(UserEntity.class));
    }
    @Test
    void createUserRoleNotFoundTest(){
        String username = "test_user";
        String rawPassword = "password";
        RoleEnum roleEnum = RoleEnum.DENTIST;

        AuthCreateUser authCreateUser = new AuthCreateUser(username, rawPassword, new AuthCreateRoleRequest(roleEnum.name()));

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(roleRepository.findByRoleTypeEnum(roleEnum)).thenReturn(Optional.empty());


        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.createUser(authCreateUser)
        );

        Assertions.assertEquals("Role not found", exception.getMessage());
        verify(userRepository, never()).save(any());

    }

     @Test
    void loginUserSuccessTest (){
         String username = "testUser";
         String password = "password";
         String token = "jwtToken";


         Role mockRole = new Role();
         mockRole.setRoleTypeEnum(RoleEnum.SECRETARIAT);
         mockRole.setRolePermissions(Set.of());

         AuthenticationManager authManager = mock(AuthenticationManager.class);
         Authentication auth = mock(Authentication.class);

         when(auth.isAuthenticated()).thenReturn(true);
         when(authManager.authenticate(any(Authentication.class))).thenReturn(auth);

         AuthLogin authLogin = new AuthLogin(username, password);

         UserEntity mockUser = new UserEntity();
         mockUser.setUsername(username);
         mockUser.setPassword("encodedPassword");
         mockUser.setRoleType(mockRole);
         mockUser.setIsEnable(true);
         mockUser.setAccountNoLocked(true);
         mockUser.setAccountNoExpired(true);
         mockUser.setCreadentialNoExpired(true);
         String authorities = "ROLE_" + mockUser.getRoleType().getRoleTypeEnum().name();

         when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(mockUser));
         when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(true);
         when(jwtUtils.createToken(username, authorities)).thenReturn(token);
         AuthResponse authResponse = userService.loginUser(authLogin, authManager);


         assertNotNull(authResponse);
         assertEquals(username, authResponse.username());
         verify(jwtUtils, times(1)).createToken(username, authorities);
         verify(userRepository, times(1)).findUserEntityByUsername(username);
         verify(authManager, times(1)).authenticate(any(Authentication.class));
     }

    @Test
    void testLoginUserInvalidPassword() {
        String username = "testUser";
        String password = "password";
        String token = "jwtToken";


        Role mockRole = new Role();
        mockRole.setRoleTypeEnum(RoleEnum.PATIENT);
        mockRole.setRolePermissions(Set.of());




        AuthenticationManager authManager = mock(AuthenticationManager.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        AuthLogin loginRequest = new AuthLogin(username, password);


        UserEntity mockUser = new UserEntity();
        mockUser.setUsername(username);
        mockUser.setPassword("encodedPassword");
        mockUser.setRoleType(mockRole);
        mockUser.setIsEnable(true);
        mockUser.setAccountNoLocked(true);
        mockUser.setAccountNoExpired(true);
        mockUser.setCreadentialNoExpired(true);
        String authorities = "ROLE_" + mockUser.getRoleType().getRoleTypeEnum().name();

        when(userRepository.findUserEntityByUsername(username)).thenThrow(new BadCredentialsException("Invalid username or password"));
        when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(false);



        // Act & Assert
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> userService.loginUser(loginRequest, authManager)
        );

        assertEquals("Invalid username or password", exception.getMessage());


        verify(userRepository, times(1)).findUserEntityByUsername(username);
        verify(passwordEncoder, never()).matches(password, mockUser.getPassword());

    }

    @Test
    void testGetUser() {
        // Arrange
        List<UserEntity> mockUsers = List.of(new UserEntity(), new UserEntity());
        when(userRepository.findAll()).thenReturn(mockUsers);

        // Act
        List<UserEntity> users = userService.getUser();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }
    @Test
    void testFindUserByIdSuccess() {
        // Arrange
        Long userId = 1L;
        UserEntity mockUser = new UserEntity();
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        UserEntity user = userService.finUserById(userId);

        // Assert
        assertNotNull(user);
        verify(userRepository, times(1)).findById(userId);
    }
    @Test
    void testFindUserByIdNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.finUserById(userId));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testDeleteUserByUsernameSuccess() {
        // Arrange
        String  username = "test_password";
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername(username);
        mockUser.setPassword("encodedPassword");
        mockUser.setRoleType(new Role());
        mockUser.setIsEnable(true);
        mockUser.setAccountNoLocked(true);
        mockUser.setAccountNoExpired(true);
        mockUser.setCreadentialNoExpired(true);

        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(mockUser));
        when(userRepository.existsById(mockUser.getIdUser())).thenReturn(true);

        // Act
        userService.deleteUserByUsername(username);

        // Assert
        verify(userRepository, times(1)).findUserEntityByUsername(username);
        verify(userRepository, times(1)).existsById(mockUser.getIdUser());
    }
    @Test
    void testDeleteUserByUsernameNotFound() {
        String username = "test_user";
        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.deleteUserByUsername(username));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findUserEntityByUsername(username);
        verify(userRepository, times(0)).existsById(anyLong());
    }

    @Test
    void testDeleteUsernameByUsernameNotExist(){
        String  username = "test_password";
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername(username);
        mockUser.setPassword("encodedPassword");
        mockUser.setRoleType(new Role());
        mockUser.setIsEnable(true);
        mockUser.setAccountNoLocked(true);
        mockUser.setAccountNoExpired(true);
        mockUser.setCreadentialNoExpired(true);
        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(mockUser));
        when(userRepository.existsById(mockUser.getIdUser())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.deleteUserByUsername(username));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findUserEntityByUsername(username);
        verify(userRepository, times(1)).existsById(mockUser.getIdUser());
    }

    @Test
    void auhtenticatedSuccessTest(){
        String username = "testUser";
        String password = "testPassword";
        String encodedPassword = "encodedPassword";
        Role mockRole = new Role();
        mockRole.setRoleTypeEnum(RoleEnum.PATIENT);
        mockRole.setRolePermissions(Set.of());
        UserEntity userEntity = new UserEntity(
                1L, username, encodedPassword,
                true, true, true, true, mockRole);

        when(userRepository.findUserEntityByUsername(username)).thenReturn(
                java.util.Optional.of(userEntity)
        );
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        // Act
        UsernamePasswordAuthenticationToken result =
                (UsernamePasswordAuthenticationToken) userService.authenticated(username, encodedPassword);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getPrincipal());
        assertEquals("encodedPassword", result.getCredentials());
    }
    @Test
    void authenticatedInvalidPassword(){
        String username = "testUser";
        String password = "wrongPassword";
        Role mockRole = new Role();
        mockRole.setRoleTypeEnum(RoleEnum.SECRETARIAT);
        mockRole.setRolePermissions(Set.of());
        UserEntity userEntity = new UserEntity(
                1L, username, "encodedPassword",
                true, true, true, true, mockRole);
        // Configura el mock para loadUserByUsername
        when(userRepository.findUserEntityByUsername(username)).thenReturn(
                java.util.Optional.of(userEntity)
        );
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false);

        // Act & Assert
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> userService.authenticated(username, password),
                "Invalid  password"
        );

        assertEquals("Invalid  password", exception.getMessage());
    }

    @Test
    void testEditUserSuccess() {
        // Arrange
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("testUser");
        mockUser.setPassword("oldPassword");
        mockUser.setIsEnable(true);
        mockUser.setAccountNoExpired(true);
        mockUser.setAccountNoLocked(true);
        mockUser.setCreadentialNoExpired(true);

        UserEntity updatedUser = new UserEntity();
        updatedUser.setUsername("testUser");
        updatedUser.setPassword("newPassword");
        updatedUser.setIsEnable(true);
        updatedUser.setAccountNoExpired(true);
        updatedUser.setAccountNoLocked(true);
        updatedUser.setCreadentialNoExpired(true);

        String encodedPassword = "encodedNewPassword";

        when(userRepository.findUserEntityByUsername(updatedUser.getUsername())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode("newPassword")).thenReturn(encodedPassword);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // Act
        UserEntity result = userService.editUser(updatedUser);

        // Assert
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).findUserEntityByUsername(updatedUser.getUsername());
        verify(userRepository, times(1)).save(mockUser);

        assertNotNull(result);
        assertEquals(encodedPassword, result.getPassword());
        assertTrue(result.getIsEnable());
        assertTrue(result.getAccountNoExpired());
        assertTrue(result.getAccountNoLocked());
        assertTrue(result.getCreadentialNoExpired());
    }
    @Test
    void testEditUserNotFound() {
        // Arrange
        UserEntity updatedUser = new UserEntity();
        updatedUser.setUsername("testUser");

        when(userRepository.findUserEntityByUsername(updatedUser.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.editUser(updatedUser));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findUserEntityByUsername(updatedUser.getUsername());
        verify(userRepository, never()).save(any());
    }
    @Test
    void testEditUserPasswordNull (){
        // Datos de prueba
        UserEntity inputUser = new UserEntity();
        inputUser.setUsername("test_user");
        inputUser.setPassword(null);
        inputUser.setIsEnable(true);
        inputUser.setAccountNoExpired(true);
        inputUser.setAccountNoLocked(true);
        inputUser.setCreadentialNoExpired(true);

        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("test_user");
        existingUser.setPassword("oldPassword");
        existingUser.setIsEnable(true);
        existingUser.setAccountNoExpired(true);
        existingUser.setAccountNoLocked(true);
        existingUser.setCreadentialNoExpired(true);


        when(userRepository.findUserEntityByUsername("test_user")).thenReturn(Optional.of(existingUser));



        UserEntity updatedUser = userService.editUser(existingUser);


        verify(userRepository).findUserEntityByUsername("test_user");
        verify(passwordEncoder, times(1)).encode("oldPassword");
        verify(userRepository).save(existingUser);

        Assertions.assertEquals(null, updatedUser.getPassword());
        Assertions.assertTrue(updatedUser.getIsEnable());
        Assertions.assertTrue(updatedUser.getAccountNoExpired());
        Assertions.assertTrue(updatedUser.getAccountNoLocked());
        Assertions.assertTrue(updatedUser.getCreadentialNoExpired());
    }
    @Test
    void testEditUserPasswordEmpty (){
        // Datos de prueba
        UserEntity inputUser = new UserEntity();
        inputUser.setUsername("testUser");
        inputUser.setPassword("");
        inputUser.setIsEnable(true);
        inputUser.setAccountNoExpired(true);
        inputUser.setAccountNoLocked(true);
        inputUser.setCreadentialNoExpired(true);

        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("testUser");
        existingUser.setPassword("oldPassword");
        existingUser.setIsEnable(true);
        existingUser.setAccountNoExpired(true);
        existingUser.setAccountNoLocked(true);
        existingUser.setCreadentialNoExpired(true);


        when(userRepository.findUserEntityByUsername("testUser")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("")).thenReturn(existingUser.getPassword());


        UserEntity updatedUser = userService.editUser(inputUser);


        verify(userRepository, times(1)).findUserEntityByUsername("testUser");
        verify(passwordEncoder, times(1)).encode("");
        verify(userRepository).save(existingUser);

        Assertions.assertEquals(existingUser.getPassword(), updatedUser.getPassword());
        Assertions.assertTrue(updatedUser.getIsEnable());
        Assertions.assertTrue(updatedUser.getAccountNoExpired());
        Assertions.assertTrue(updatedUser.getAccountNoLocked());
        Assertions.assertTrue(updatedUser.getCreadentialNoExpired());
    }

    @Test
    void testEditUserPasswordEncoding() {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("newPassword");

        UserEntity userFinding = new UserEntity();
        userFinding.setUsername("testUser");
        userFinding.setPassword("oldPassword");


        when(userRepository.findUserEntityByUsername("testUser")).thenReturn(Optional.of(userFinding));
        when(passwordEncoder.encode("newPassword")).thenReturn("encryptedPassword");


        UserEntity updatedUser = userService.editUser(userEntity);


        assertEquals("encryptedPassword", updatedUser.getPassword());
        verify(passwordEncoder).encode("newPassword");
    }

}
