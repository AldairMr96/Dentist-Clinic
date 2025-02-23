package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.dto.AuthCreateUser;
import com.mycompany.clinica_odontologica.dto.AuthLogin;
import com.mycompany.clinica_odontologica.dto.AuthResponse;
import com.mycompany.clinica_odontologica.model.Role;
import com.mycompany.clinica_odontologica.model.RoleEnum;
import com.mycompany.clinica_odontologica.model.UserEntity;
import com.mycompany.clinica_odontologica.repository.IRoleRepository;
import com.mycompany.clinica_odontologica.repository.IUserRepository;
import com.mycompany.clinica_odontologica.utils.JWTUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService, IUserService {

@Autowired
private IUserRepository userRepository;
@Autowired
private IRoleRepository roleRepository;
@Autowired
private JWTUtils jwtUtils;
@Autowired
private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntityFinding = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add( new SimpleGrantedAuthority("ROLE_".concat(userEntityFinding.getRoleType().getRoleTypeEnum().name())));
        userEntityFinding.getRoleType().getRolePermissions()
                .forEach(rolePermissions -> authorities.add( new SimpleGrantedAuthority(rolePermissions.getPermissionEntity().getNamePermission())));
        User userDetail = new User(
                userEntityFinding.getUsername(),
                userEntityFinding.getPassword(),
                userEntityFinding.getIsEnable(),
                userEntityFinding.getAccountNoExpired(),
                userEntityFinding.getCreadentialNoExpired(),
                userEntityFinding.getAccountNoLocked(),
                authorities
        );
        return userDetail;
    }
    public Authentication authenticated (String username, String password){
        UserDetails userDetail= this.loadUserByUsername(username);


        if (!passwordEncoder.matches(password, userDetail.getPassword())){
            throw new BadCredentialsException("Invalid  password");
        }
        return new UsernamePasswordAuthenticationToken(username, userDetail.getPassword(), userDetail.getAuthorities());
    }
    @Override
    public void createUser(AuthCreateUser authCreateUser) {
        String username = authCreateUser.username().trim();
        String password = passwordEncoder.encode(authCreateUser.password());
        RoleEnum roleEnum = RoleEnum.valueOf(authCreateUser.roleRequest().roleName());

        if(userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("The user already exists");
        }
        Role roleFinding = roleRepository.findByRoleTypeEnum(roleEnum)
                .orElseThrow(() -> new EntityNotFoundException("Role not found") );
        UserEntity userEntityCreated =new UserEntity();
        userEntityCreated.setUsername(username);
        userEntityCreated.setPassword(password);
        userEntityCreated.setRoleType(roleFinding);
        userEntityCreated.setIsEnable(true);
        userEntityCreated.setAccountNoExpired(true);
        userEntityCreated.setAccountNoLocked(true);
        userEntityCreated.setCreadentialNoExpired(true);

        userRepository.save(userEntityCreated);
    }

    @Override
    public List<UserEntity> getUser() {

        List<UserEntity> users =userRepository.findAll();
        return users;
    }

    @Override
    public UserEntity finUserById(Long idUser) {
        UserEntity userFinding = userRepository.findById(idUser)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
        return userFinding;
    }

    @Override
    public void deleteUserById(Long idUser) {
        if (!userRepository.existsById(idUser)){
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(idUser);
    }

    @Override
    public UserEntity editUser(UserEntity userEntity) {
        UserEntity userFinding = userRepository.findUserEntityByUsername(userEntity.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (userEntity.getPassword() != null && !userEntity.getPassword().isEmpty()){
            String passwordCrypted =this.passwordEncoder.encode(userEntity.getPassword());
            userFinding.setPassword(passwordCrypted);
        }

        userFinding.setIsEnable(userEntity.getIsEnable());
        userFinding.setAccountNoExpired(userEntity.getAccountNoExpired());
        userFinding.setAccountNoLocked(userEntity.getAccountNoLocked());
        userFinding.setCreadentialNoExpired(userEntity.getCreadentialNoExpired());

        userRepository.save(userFinding);

        return userFinding;
    }

    @Override
    public AuthResponse loginUser(AuthLogin authLogin) {
        String username = authLogin.username();
        String password = authLogin.password();

        Authentication authToken = this.authenticated(username, password);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        String accessToken = jwtUtils.createToken(authToken);

        AuthResponse response = new AuthResponse(username, "Login susscessfully", accessToken, true);

        return response;
    }


}
