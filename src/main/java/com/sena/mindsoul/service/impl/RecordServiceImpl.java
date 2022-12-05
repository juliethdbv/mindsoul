package com.sena.mindsoul.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.mindsoul.entity.Record;
import com.sena.mindsoul.service.RecordService;
import com.sena.mindsoul.repository.RecordRepository;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Override
    public List<Record> findAll() {
        return (List<Record>) recordRepository.findAll();
    }

    @Override
    public Record findOne(Long id) {
        return recordRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Record record) {
        recordRepository.save(record);
    }

}
