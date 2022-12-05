package com.sena.mindsoul.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.mindsoul.repository.RoleRepository;
import com.sena.mindsoul.service.RoleService;
import com.sena.mindsoul.entity.Role;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return (List<Role>) roleRepository.findAll();
    }

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Role findOne(Long id) {
        return roleRepository.findById(id).orElse(null);
    }
    
}
