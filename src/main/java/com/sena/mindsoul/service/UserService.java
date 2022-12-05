package com.sena.mindsoul.service;

import java.util.List;

import com.sena.mindsoul.dto.UserDto;
import com.sena.mindsoul.entity.User;

public interface UserService {

    public List<User> findAll();

    void saveUser(UserDto userDto);

    void saveAdmin(UserDto userDto);

    void savePsicologo(UserDto userDto);

    void save(User user);

    User findByEmail(String email);

    public User findOne(long id);

    List<UserDto> findAllUsers();

}
