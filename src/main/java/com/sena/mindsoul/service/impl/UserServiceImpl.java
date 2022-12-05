package com.sena.mindsoul.service.impl;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sena.mindsoul.dto.UserDto;
import com.sena.mindsoul.entity.Role;
import com.sena.mindsoul.entity.User;
import com.sena.mindsoul.repository.RoleRepository;
import com.sena.mindsoul.repository.UserRepository;
import com.sena.mindsoul.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        
        return (List<User>) userRepository.findAll();
    }

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findOne(long id) {
        return userRepository.findById(id).orElse(null);
    }


    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        // el Dto es el de manipulación de datos por eso en la entity guardamos el nombre pero en el dto pedimos el
        // nombre y apellido
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());

        //encrypt the password once we integrate spring security
        //user.setPassword(userDto.getPassword());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // buscar si existe el ROLE_PACIENTE en caso de que sea null invocamos el metodo para que lo cree
        Role role = roleRepository.findByName("ROLE_USER");
        if(role == null){
            role = checkRoleExist2();
        }
        //asignamos los roles a un arreglo de lo que devuelve el metodo
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    // crea el ROLE_PACIENTE si no existe esto se hace por defecto
    private Role checkRoleExist2() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }

    @Override
    public void saveAdmin(UserDto userDto) {
        User user = new User();
        // el Dto es el de manipulación de datos por eso en la entity guardamos el nombre pero en el dto pedimos el
        // nombre y apellido
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());

        //encrypt the password once we integrate spring security
        //user.setPassword(userDto.getPassword());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // buscar si existe el ROLE_USER en caso de que sea null invocamos el metodo para que lo cree
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null){
            role = checkRoleExist1();
        }
        //asignamos los roles a un arreglo de lo que devuelve el metodo
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    // crea el ROLE_ADMIN si no existe esto se hace por defecto
    private Role checkRoleExist1() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    @Override
    public void savePsicologo(UserDto userDto) {
        User user = new User();
        // el Dto es el de manipulación de datos por eso en la entity guardamos el nombre pero en el dto pedimos el
        // nombre y apellido
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());

        //encrypt the password once we integrate spring security
        //user.setPassword(userDto.getPassword());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // buscar si existe el ROLE_PSICOLOGO en caso de que sea null invocamos el metodo para que lo cree
        Role role = roleRepository.findByName("ROLE_PSICOLOGO");
        if(role == null){
            role = createRolePsicologo();
        }
        //asignamos los roles a un arreglo de lo que devuelve el metodo
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    // crea el ROLE_PSICOLOGO si no existe esto se hace por defecto
    private Role createRolePsicologo() {
        Role role = new Role();
        role.setName("ROLE_PSICOLOGO");
        return roleRepository.save(role);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    // se usa para que cuando mostremos los usuarios podamos mostrar el nombre y apellido por separado
    private UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        String[] name = user.getName().split(" ");
        userDto.setFirstName(name[0]);
        userDto.setLastName(name[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

}
