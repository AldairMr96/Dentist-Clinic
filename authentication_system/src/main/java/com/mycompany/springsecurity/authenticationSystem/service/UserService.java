package com.mycompany.springsecurity.authenticationSystem.service;

import com.mycompany.springsecurity.authenticationSystem.dto.AuthCreateUserRequest;
import com.mycompany.springsecurity.authenticationSystem.dto.AuthLoginRequest;
import com.mycompany.springsecurity.authenticationSystem.dto.AuthResponse;
import com.mycompany.springsecurity.authenticationSystem.model.Role;
import com.mycompany.springsecurity.authenticationSystem.model.RolesEnum;
import com.mycompany.springsecurity.authenticationSystem.model.UserEntity;
import com.mycompany.springsecurity.authenticationSystem.repository.IRoleRepository;
import com.mycompany.springsecurity.authenticationSystem.repository.IUserRepository;
import com.mycompany.springsecurity.authenticationSystem.util.JwtUtils;
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

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
            IUserRepository userRepository,
            IRoleRepository roleRepository,
            JwtUtils jwtUtils,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userFinding = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_".concat(userFinding.getRoleType().getRoleTypeEnum().name())));
        userFinding.getRoleType().getRolePermissions()
                .forEach(rolePermission -> authorities.add(new SimpleGrantedAuthority(rolePermission.getPermission().getNamePermission())));


        User userDetail = new User(
                userFinding.getUsername(),
                userFinding.getPassword(),
                userFinding.getIsEnable(),
                userFinding.getAccountNoExpired(),
                userFinding.getCreadentialNoExpired(),
                userFinding.getAccountNoLocked(),
                authorities
                );

        return userDetail;
    }



    @Override
    public List<UserEntity> getUser() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities;
    }

    @Override
    public UserEntity finUserById(Long idUserEntity) {
        UserEntity userFinding = userRepository.findById(idUserEntity)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
        return userFinding;
    }

    @Override
    public void deleteUserById(Long idUserEntity) {
      if (!userRepository.existsById(idUserEntity)){
          throw new EntityNotFoundException("User not found");
      }

      userRepository.deleteById(idUserEntity);

    }

    @Override
    public UserEntity editUser(UserEntity userEntity) {


        UserEntity userFinding = userRepository.findUserEntityByUsername(userEntity.getUsername() )
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (userEntity.getPassword() != null && !userEntity.getPassword().isEmpty()) {
            String encodedPassword = this.passwordEncoder.encode(userEntity.getPassword());
            userFinding.setPassword(encodedPassword);
        }
        userFinding.setIsEnable(userEntity.getIsEnable());
        userFinding.setAccountNoExpired(userEntity.getAccountNoExpired());
        userFinding.setAccountNoLocked(userEntity.getAccountNoLocked());
        userFinding.setCreadentialNoExpired(userEntity.getCreadentialNoExpired());

       userRepository.save(userFinding);

        return userFinding;
    }


    public AuthResponse loginUser (AuthLoginRequest authLoginRequest){

        String username = authLoginRequest.username();
        String password = authLoginRequest.password();
        Authentication authenticationToken = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String accessToken = jwtUtils.createToken(authenticationToken);


        AuthResponse authResponse = new AuthResponse(username, "Login susscessfully", accessToken, true);
        return authResponse;

    }
    public Authentication authenticate(String username, String password){
        UserDetails userDetail= this.loadUserByUsername(username);


        if (!passwordEncoder.matches(password, userDetail.getPassword())){
            throw new BadCredentialsException("Invalid  password");
        }
        return new UsernamePasswordAuthenticationToken(username, userDetail.getPassword(), userDetail.getAuthorities());
    }
    @Override
    public void createUser(AuthCreateUserRequest authCreateUserRequest) {
        String usernameRequest = authCreateUserRequest.username().trim();
        String passwordRequest = passwordEncoder.encode(authCreateUserRequest.password());
        RolesEnum roleEnumRequest = RolesEnum.valueOf (authCreateUserRequest.roleRequest().roleName());
        if (userRepository.existsByUsername(usernameRequest)) {
            throw new IllegalArgumentException("The user already exists");
        }

        Role roleFinding = roleRepository.findByRoleTypeEnum(roleEnumRequest)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));


        UserEntity userEntity =  new UserEntity();
        userEntity.setUsername(usernameRequest);
        userEntity.setPassword(passwordRequest);
        userEntity.setRoleType(roleFinding);
        userEntity.setIsEnable(true);
        userEntity.setAccountNoExpired(true);
        userEntity.setAccountNoLocked(true);
        userEntity.setCreadentialNoExpired(true);


        userRepository.save(userEntity);



    }


}
