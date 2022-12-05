package com.sena.mindsoul.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sena.mindsoul.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository <Appointment, Long> {
    @Query(value = "SELECT * FROM appointment a WHERE a.doctor = ?1", nativeQuery = true)
    List <Appointment> findAppointmentByDoctorName(String nameDoctor);
}
