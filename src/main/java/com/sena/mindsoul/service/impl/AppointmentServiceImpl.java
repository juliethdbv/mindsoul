package com.sena.mindsoul.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.mindsoul.entity.Appointment;
import com.sena.mindsoul.repository.AppointmentRepository;
import com.sena.mindsoul.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService{

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> findAll() {
        
        return (List<Appointment>) appointmentRepository.findAll();
    }

    @Override
    public void save(Appointment appointment) {
        appointmentRepository.save(appointment);
        
    }

    @Override
    public Appointment findOne(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        appointmentRepository.deleteById(id);
    }
    
}
