package com.sena.mindsoul.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.mindsoul.entity.Specialty;
import com.sena.mindsoul.repository.SpecialtyRepository;
import com.sena.mindsoul.service.SpecialtyService;

@Service
public class SpecialtyServiceImpl implements SpecialtyService{

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Override
    public List<Specialty> findAll() {
        return (List<Specialty>) specialtyRepository.findAll();
    }

    @Override
    public void save(Specialty specialty) {
        specialtyRepository.save(specialty);
    }

    @Override
    public Specialty findOne(Long id) {
        return specialtyRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        specialtyRepository.deleteById(id);
    }
    
}
