package com.project.hospitalReport.service;

import com.project.hospitalReport.entity.DrugsStock;
import com.project.hospitalReport.repository.DrugsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DrugsService {

    @Autowired
    DrugsRepo drugsRepo;

    public DrugsStock addDrugs(DrugsStock stock) {
        return drugsRepo.save(stock);
    }

    public Optional<DrugsStock> findByName(DrugsStock stock) {
        return drugsRepo.findByName(stock.getName());
    }
}
