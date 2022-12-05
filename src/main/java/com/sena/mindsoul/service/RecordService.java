package com.sena.mindsoul.service;

import java.util.List;

import com.sena.mindsoul.entity.Record;

public interface RecordService {

    public List <Record> findAll();
    public Record findOne(Long id);
    public void save(Record record);
    
}
