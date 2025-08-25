package com.project.hospitalReport.service;

import com.project.hospitalReport.entity.DrugLog;
import com.project.hospitalReport.entity.DrugsStock;
import com.project.hospitalReport.repository.DrugsLogRepo;
import com.project.hospitalReport.repository.DrugsRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class DrugsService {

    @Autowired
    DrugsRepo drugsRepo;

    @Autowired
    DrugsLogRepo drugsLogRepo;

    public DrugsStock addDrug(DrugsStock stock) {
        return drugsRepo.save(stock);
    }

    public DrugLog addLog(DrugLog log) {
        return drugsLogRepo.save(log);
    }

    public Optional<DrugsStock> findByName(DrugsStock stock) {
        return drugsRepo.findByName(stock.getName());
    }

    public String updateDrug(DrugsStock updatedStock) {
        DrugsStock actualStock = drugsRepo.getById(updatedStock.getId());
        if (actualStock != null) {

            actualStock.setAddedDate(updatedStock.getAddedDate());
            actualStock.setMrp(updatedStock.getMrp());
            actualStock.setName(updatedStock.getName());
            actualStock.setPerPieceRate(updatedStock.getPerPieceRate());
            actualStock.setUpdatedDate(updatedStock.getUpdatedDate());

            DrugLog drugLog = new DrugLog();
            Long difference = updatedStock.getQuantity() - actualStock.getQuantity();
            if (difference > 0) {
                drugLog.setAddedQuantity(difference);
                drugLog.setSoldQuantity(0L);
            } else {
                Long positive = (difference < 0) ? -difference : difference;
                drugLog.setSoldQuantity(positive);
                drugLog.setAddedQuantity(0L);
            }
            drugLog.setDrugName(updatedStock.getName());
            drugLog.setAvailableQuantity(updatedStock.getQuantity());
            drugLog.setUpdatedDate(updatedStock.getUpdatedDate());
            drugLog.setUpdatedTime(LocalTime.now());

            drugLog.setStock(actualStock);
            drugsLogRepo.save(drugLog);

            actualStock.setQuantity(updatedStock.getQuantity());
            drugsRepo.save(actualStock);
            return "Drug Updated Successfully!!!";
        }
        return "No Drug Found!!?";
    }

    public String deleteDrug(DrugsStock stock) {
        DrugsStock drugsStock = drugsRepo.getById(stock.getId());
        try {
            drugsRepo.deleteById(drugsStock.getId());
            return "Drug Deleted Successfully";
        } catch (EntityNotFoundException e) {
            return "Can't Find Drug To Delete";
        }
    }
    public List<DrugsStock> get()
    {
        return drugsRepo.findAll();
    }
}
