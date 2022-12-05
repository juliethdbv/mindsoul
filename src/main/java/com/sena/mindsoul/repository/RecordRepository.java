package com.sena.mindsoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sena.mindsoul.entity.Record;

public  interface RecordRepository extends JpaRepository<Record, Long> {
    
}
