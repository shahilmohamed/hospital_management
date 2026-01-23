package com.project.hospitalReport.service;

import com.project.hospitalReport.entity.DrugLog;
import com.project.hospitalReport.entity.DrugsStock;
import com.project.hospitalReport.repository.DrugsLogRepo;
import com.project.hospitalReport.repository.DrugsRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
            actualStock.setUpdatedDate(LocalDate.now());

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
            drugLog.setUpdatedDate(LocalDate.now());
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

    public List<DrugsStock> get() {
        return drugsRepo.findAll();
    }

    public Optional<DrugsStock> findById(Long id) {
        return drugsRepo.findById(id);
    }

    public DrugsStock getById(Long id) {
        return drugsRepo.getById(id);
    }

    public List<DrugsStock> getParticularDrugs(List<Long> ids) {
        return drugsRepo.findAllById(ids);
    }

    public Page<DrugsStock> getDrugPage(int page, int size)
    {
        return drugsRepo.findAll(PageRequest.of(page, size));
    }

    public Page<DrugsStock> searchDrug(String search, Pageable pageable)
    {
        return drugsRepo.findByNameContainingIgnoreCase(search, pageable);
    }

    public Page<DrugLog> getLogById(Long id, Pageable pageable) {
        DrugsStock stock = drugsRepo.getById(id);
        return drugsLogRepo.findByStock(stock, pageable);
    }

    public Page<DrugLog> getFilterLog(LocalDate fromDate, LocalDate toDate, Long id, Pageable pageable)
    {
        DrugsStock stock = drugsRepo.getById(id);
        return drugsLogRepo.findByStockAndUpdatedDateBetween(stock, fromDate, toDate, pageable);
    }
}
