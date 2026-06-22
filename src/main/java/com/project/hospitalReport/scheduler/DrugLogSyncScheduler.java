package com.project.hospitalReport.scheduler;

import com.project.hospitalReport.service.DrugLogMongoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DrugLogSyncScheduler {

    private static final Logger log = LoggerFactory.getLogger(DrugLogSyncScheduler.class);

    @Autowired
    private DrugLogMongoService drugLogMongoService;

    // Run daily at 10:00 PM
    @Scheduled(cron = "${drug.log.sync.cron}", zone = "${drug.log.sync.time-zone}")
    public void syncDrugLogsDaily() {
        log.info("Starting daily drug log sync to MongoDB...");
        try {
            System.out.println("Starting daily drug log sync to MongoDB...");
            System.out.println("Starting time: " + LocalDateTime.now());
            int syncedCount = drugLogMongoService.syncAllDrugLogsToMongo();
            System.out.println("Ending time: " + LocalDateTime.now());
            log.info("Daily drug log sync completed. Synced {} records.", syncedCount);
        } catch (Exception e) {
            log.error("Daily drug log sync failed: {}", e.getMessage());
        }
    }
}
