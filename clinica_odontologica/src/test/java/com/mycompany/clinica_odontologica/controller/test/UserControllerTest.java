package com.mycompany.clinica_odontologica.controller.test;

import com.mycompany.clinica_odontologica.controller.UserController;
import com.mycompany.clinica_odontologica.dto.AuthCreateRoleRequest;
import com.mycompany.clinica_odontologica.dto.AuthCreateUser;
import com.mycompany.clinica_odontologica.dto.AuthLogin;
import com.mycompany.clinica_odontologica.dto.AuthResponse;
import com.mycompany.clinica_odontologica.model.*;
import com.mycompany.clinica_odontologica.repository.IUserRepository;
import com.mycompany.clinica_odontologica.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

public class UserControllerTest {
    @Mock
    private IUserService userService;

    @Mock
    private AuthenticationManager authManager;
    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetUsersSuccess() {
        // Arrange
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        List<UserEntity> users = List.of(user1, user2);
        when(userService.getUser()).thenReturn(users);

        // Act
        List<UserEntity> usersResult = userController.getUsers();

        // Assert
        assertNotNull(usersResult);
        assertEquals(2, usersResult.size());
        verify(userService, times(1)).getUser();
    }

    @Test
    void testGetUsersEmpty (){
        List<UserEntity> userEntities = new ArrayList<>();

        List<UserEntity> result = userController.getUsers();

        assertNotNull(result);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindUserByIdSuccess (){
        Long idUser = 1L;
        UserEntity user = new UserEntity();
        when(userService.finUserById(idUser)).thenReturn(user);

        ResponseEntity<?> response = userController.findUserById(idUser);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).finUserById(idUser);
    }
    @Test
    void testFindUserByIdNotFound (){
        Long idUser = 1L;
        when(userService.finUserById(idUser)).thenThrow(new EntityNotFoundException("User not found"));

        ResponseEntity<?> response = userController.findUserById(idUser);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).finUserById(idUser);
    }

    @Test
    void testFindUserByIdInternalServer (){
        Long idUser = 1L;
        doThrow(new RuntimeException("Server internal Error")).when(userService).finUserById(idUser);

        ResponseEntity<?> response = userController.findUserById(1L);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());

        verify(userService, times(1)).finUserById(1L);
    }

    @Test
    void testCreateUserSucces (){
        String username = "user_test";
        String password = "password";
        String token = "token";
        RoleEnum roleEnum = RoleEnum.DENTIST;

        AuthCreateUser authCreateUser = new AuthCreateUser(username,password, new AuthCreateRoleRequest(roleEnum.name()));

        AuthResponse authResponse = new AuthResponse(username, "Create user successfully", token, true);
        when(userService.createUser(authCreateUser)).thenReturn(authResponse);

        ResponseEntity<?> response = userController.createUser(authCreateUser);

        assertEquals(OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
        verify(userService, times(1)).createUser(authCreateUser);

    }

    @Test
    void editUserSuccessTest (){
        UserEntity userEntity = new UserEntity(1L, "test_user", "passwdord", true, true, true, true, new Role() );

        when(userService.editUser(any(UserEntity.class))).thenReturn(userEntity);
        ResponseEntity<?> response = userController.editUser(userEntity);

        assertEquals(OK, response.getStatusCode());
        assertEquals(userEntity, response.getBody());
        verify(userService, times(1)).editUser(userEntity);
    }

    @Test
    void editUserNotFoundTest () {
        UserEntity userEntity = new UserEntity();
        when(userService.editUser(userEntity)).thenThrow(new EntityNotFoundException("User not found"));

        ResponseEntity<?> response = userController.editUser(userEntity);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).editUser(userEntity);

    }
    @Test
    void editUserInternalServerTest () {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        when(userService.editUser(userEntity)).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity<?> response = userController.editUser(userEntity);


        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(userService, times(1)).editUser(userEntity);

    }

    @Test
    void testDeleteUserSuccess(){
        String username = "test_user";
        RoleEnum roleEnum = RoleEnum.DENTIST;
        PermissionsEntity permissions = new PermissionsEntity(1L, "READ", new HashSet<>());
        RolePermissions rolePermissions = new RolePermissions();
        Role roleType = new Role();
        rolePermissions.setIdRolePermission(1L);
        rolePermissions.setRole(roleType);
        rolePermissions.setPermissionEntity(permissions);
        roleType.setIdRole(1L);
        roleType.setRoleTypeEnum(roleEnum);

        UserEntity userEntity = new UserEntity(
                1L, "test_user", "password", true, true, true, true, roleType);

        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(userEntity));
        doNothing().when(userService).deleteUserByUsername(username);

        ResponseEntity<?>response = userController.deleteUser(username);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Delete user susccessfully", response.getBody());

        verify(userService, times(1)).deleteUserByUsername(username);

    }

    @Test
    void testDeleteUserNotFound (){
        String username = "test_user";

       doThrow(new EntityNotFoundException("User not found")).when(userService).deleteUserByUsername(username);

       ResponseEntity<?> response = userController.deleteUser(username);

       assertEquals(NOT_FOUND, response.getStatusCode());
       assertEquals("User not found", response.getBody());
       verify(userService, times(1)).deleteUserByUsername(username);
    }
    @Test
    void testDeleteUserInternalError (){
        String username = "test_user";

        doThrow(new RuntimeException("Server internal Error")).when(userService).deleteUserByUsername(username);

        ResponseEntity<?> response = userController.deleteUser(username);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(userService, times(1)).deleteUserByUsername(username);
    }

    @Test
    void  loginUserSuccessTest (){
        String username = "test_user";
        String password = "password";
        String token = "access_token";

        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        AuthLogin authLogin = new AuthLogin(username, password);

        AuthResponse authResponse = new AuthResponse(username, "Login Successfully", token, true);

        when(userService.loginUser(authLogin, authManager)).thenReturn(authResponse);

        ResponseEntity<?> response = userController.loginUser(authLogin);

        assertEquals(OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
        verify(userService, times(1)).loginUser(authLogin, authManager);
    }

    @Test
    void loginUserBadCredetialTest (){
        String username = "user_test";
        String password = "password";

        AuthLogin authLogin = new AuthLogin(username, password);

        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.empty());
        when(userService.loginUser(authLogin, authManager)).thenThrow(new BadCredentialsException("Invalid username or password"));

        ResponseEntity<?> response = userController.loginUser(authLogin);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
        verify(userService, times(1)).loginUser(authLogin, authManager);
    }

    @Test
    void loginUserInternalErrorTest (){
        String username = "user_test";
        String password = "password";

        AuthLogin authLogin = new AuthLogin(username, password);


        doThrow(new RuntimeException("Server internal Error")).when(userService).loginUser(authLogin, authManager);

        ResponseEntity<?> response = userController.loginUser(authLogin);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(userService, times(1)).loginUser(authLogin, authManager);
    }

}
