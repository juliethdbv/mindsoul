package com.sena.mindsoul.service;

import java.util.List;

import com.sena.mindsoul.entity.Appointment;

public interface AppointmentService{
    
    public List <Appointment> findAll();
    public void save(Appointment cita);
    public Appointment findOne(Long id);
    public void delete(Long id);
}
