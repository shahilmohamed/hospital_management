package com.project.hospitalReport.service;

import com.project.hospitalReport.document.DrugLogHistory;
import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.entity.DrugLog;
import com.project.hospitalReport.repository.DrugLogHistoryRepository;
import com.project.hospitalReport.repository.DrugsLogRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DrugLogMongoService {

    @Autowired
    private DrugLogHistoryRepository drugLogHistoryRepository;

    @Autowired
    private DrugsLogRepo drugsLogRepo;

    public void saveUpdateHistory(DrugLog drugLog, Doctor doctor, Long stockId) {
        DrugLogHistory history = new DrugLogHistory();
        history.setMysqlLogId(drugLog.getId());
        history.setStockId(stockId);
        history.setDrugName(drugLog.getDrugName());
        history.setAddedQuantity(drugLog.getAddedQuantity());
        history.setSoldQuantity(drugLog.getSoldQuantity());
        history.setAvailableQuantity(drugLog.getAvailableQuantity());
        history.setUpdatedDate(drugLog.getUpdatedDate());
        history.setUpdatedTime(drugLog.getUpdatedTime());
        history.setAction(drugLog.getAction());

        if (doctor != null) {
            history.setDoctorId(doctor.getId());
            history.setModifiedBy(doctor.getFirstname() + " " + doctor.getLastname());
        }

        drugLogHistoryRepository.save(history);
    }

    public void saveHistory(DrugLog drugLog){
        DrugLogHistory history = new DrugLogHistory();
        history.setMysqlLogId(drugLog.getId());
        history.setStockId(drugLog.getStock().getId());
        history.setDrugName(drugLog.getDrugName());
        history.setAddedQuantity(drugLog.getAddedQuantity());
        history.setSoldQuantity(drugLog.getSoldQuantity());
        history.setAvailableQuantity(drugLog.getAvailableQuantity());
        history.setUpdatedDate(drugLog.getUpdatedDate());
        history.setUpdatedTime(drugLog.getUpdatedTime());
        history.setAction(drugLog.getAction());
        Doctor doctor = drugLog.getDoctor();
        if (doctor != null) {
            history.setDoctorId(doctor.getId());
            history.setModifiedBy(doctor.getFirstname() + " " + doctor.getLastname());
        }
        drugLogHistoryRepository.save(history);
    }

    public Page<DrugLogHistory> getLogByStockId(Long stockId, Pageable pageable) {
        return drugLogHistoryRepository.findByStockId(stockId, pageable);
    }

}
