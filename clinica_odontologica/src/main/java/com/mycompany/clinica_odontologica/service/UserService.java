package com.mycompany.clinica_odontologica.service;

import com.mycompany.clinica_odontologica.dto.AuthCreateUser;
import com.mycompany.clinica_odontologica.dto.AuthLogin;
import com.mycompany.clinica_odontologica.dto.AuthResponse;
import com.mycompany.clinica_odontologica.model.Role;
import com.mycompany.clinica_odontologica.model.RoleEnum;
import com.mycompany.clinica_odontologica.model.UserEntity;
import com.mycompany.clinica_odontologica.repository.IRoleRepository;
import com.mycompany.clinica_odontologica.repository.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
import java.util.Objects;

@Service
public class UserService implements UserDetailsService, IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository,
                       IRoleRepository roleRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

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

    @Override
    @Transactional
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
            //String passwordCrypted =this.passwordEncoder.encode(userEntity.getPassword());
            String passwordCrypted = "admin";
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
    public AuthResponse loginUser(AuthLogin authLogin, AuthenticationManager authenticationManager) {
        String username = authLogin.username();
        String password = authLogin.password();

        try {
            Authentication authToken = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);

            UserDetails userDetails = this.loadUserByUsername(username);

            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER");

            String accessToken = jwtService.generateToken(userDetails.getUsername(), role);

            return new AuthResponse(username, "Login successfully", accessToken, true);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public Authentication authenticated (String username, String password){
        UserDetails userDetail= this.loadUserByUsername(username);

        // !passwordEncoder.matches(password, userDetail.getPassword())
        if (!Objects.equals(password, userDetail.getPassword())){
            throw new BadCredentialsException("Invalid  password");
        }
        return new UsernamePasswordAuthenticationToken(username, userDetail.getPassword(), userDetail.getAuthorities());
    }

}
