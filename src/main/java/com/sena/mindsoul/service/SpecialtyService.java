package com.sena.mindsoul.service;

import java.util.List;

import com.sena.mindsoul.entity.Specialty;

public interface SpecialtyService{

    public List <Specialty> findAll();

    public void save(Specialty specialty);

    public Specialty findOne(Long id);
    
    public void delete(Long id);
}
