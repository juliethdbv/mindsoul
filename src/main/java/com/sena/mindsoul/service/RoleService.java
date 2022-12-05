package com.sena.mindsoul.service;

import java.util.List;

import com.sena.mindsoul.entity.Role;

public interface RoleService {
    
    public List <Role> findAll();

    public void save(Role role);

    public Role findOne(Long id);

}
