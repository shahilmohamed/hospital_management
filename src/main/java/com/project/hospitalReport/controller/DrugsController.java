package com.project.hospitalReport.controller;

import com.project.hospitalReport.dto.ApiResponse;
import com.project.hospitalReport.dto.PageRequ;
import com.project.hospitalReport.entity.DrugLog;
import com.project.hospitalReport.entity.DrugsStock;
import com.project.hospitalReport.service.DrugsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
@RequestMapping("/drugs")
public class DrugsController {

    @Autowired
    DrugsService drugsService;

    @PostMapping("/addDrug")
    public ApiResponse<DrugsStock> addDrugs(@RequestBody DrugsStock stock) {
        Optional<DrugsStock> drugs = drugsService.findByName(stock);
        if (drugs.isEmpty()) {
            try {
                ApiResponse<DrugsStock> response = new ApiResponse<>();
                DrugsStock d = new DrugsStock();
                d.setAddedDate(stock.getAddedDate());
                d.setMrp(stock.getMrp());
                d.setName(stock.getName());
                d.setPerPieceRate(stock.getPerPieceRate());
                d.setQuantity(stock.getQuantity());
                d.setUpdatedDate(stock.getUpdatedDate());
                drugsService.addDrug(d);

                DrugLog dl = new DrugLog();
                dl.setDrugName(stock.getName());
                dl.setAddedQuantity(stock.getQuantity());
                dl.setAvailableQuantity(stock.getQuantity());
                dl.setSoldQuantity(0L);
                dl.setUpdatedDate(stock.getUpdatedDate());
                dl.setUpdatedTime(LocalTime.now());
                dl.setStock(d);
                drugsService.addLog(dl);

                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Drugs Added Successfully");
                response.setData(null);
                return response;
            } catch (Exception e) {
                return new ApiResponse<>(null, e.toString(), HttpStatus.NO_CONTENT.value());
            }
        } else {
            return new ApiResponse<>(null, "Drug Already Present", HttpStatus.NO_CONTENT.value());
        }
    }

    @PutMapping("/updateDrug")
    public ApiResponse<String> updateDrug(@RequestBody DrugsStock stock) {
        String result = drugsService.updateDrug(stock);
        if (!result.equals("Drug Updated Successfully!!!")) {
            ApiResponse<String> response = new ApiResponse<>(null, result, HttpStatus.NO_CONTENT.value());
            return response;
        } else {
            ApiResponse<String> response = new ApiResponse<>(null, result, HttpStatus.OK.value());
            return response;
        }
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deletedrug(@RequestBody DrugsStock stock) {
        String result = drugsService.deleteDrug(stock);
        if (result.equals("Drug Deleted Successfully")) {
            ApiResponse<String> response = new ApiResponse<>(null, result, HttpStatus.OK.value());
            return response;
        } else {
            ApiResponse<String> response = new ApiResponse<>(null, result, HttpStatus.NO_CONTENT.value());
            return response;
        }
    }

    @GetMapping("/getDrugs")
    public ApiResponse<List<Map<String, Object>>> getDrugs() {
        List<DrugsStock> stocks = drugsService.get();
        ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>();
        List<Map<String, Object>> result = new ArrayList<>();
        if (stocks.size() > 0) {
            for (DrugsStock d : stocks) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", d.getId());
                map.put("addedDate", d.getAddedDate());
                map.put("mrp", d.getMrp());
                map.put("name", d.getName());
                map.put("perPieceRate", d.getPerPieceRate());
                map.put("quantity", d.getQuantity());
                map.put("updatedDate", d.getUpdatedDate());
                result.add(map);
            }
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Drugs Found");
            response.setData(result);
            return response;
        }
        response.setStatus(HttpStatus.NO_CONTENT.value());
        response.setMessage("Drugs Not Found");
        response.setData(null);
        return response;
    }

    @PostMapping("/getDrugById")
    public ApiResponse<Map<String, Object>> getById(@RequestBody DrugsStock stock) {
        DrugsStock stocks = drugsService.getById(stock.getId());
        ApiResponse<Map<String, Object>> response = new ApiResponse<>();
        if (stocks != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", stocks.getId());
            map.put("addedDate", stocks.getAddedDate());
            map.put("mrp", stocks.getMrp());
            map.put("name", stocks.getName());
            map.put("perPieceRate", stocks.getPerPieceRate());
            map.put("quantity", stocks.getQuantity());
            map.put("updatedDate", stocks.getUpdatedDate());

            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Drug Found");
            response.setData(map);
            return response;
        }
        response.setStatus(HttpStatus.NO_CONTENT.value());
        response.setMessage("Drug Not Found");
        response.setData(null);
        return response;
    }

    @PostMapping("/getDrugPage")
    public ResponseEntity<Map<String, Object>> getDrugPage(@RequestBody PageRequ pageRequ) {
        Page<DrugsStock> medicines;
        if (pageRequ.getSearch().isEmpty()) {
            medicines = drugsService.getDrugPage(pageRequ.getPage(), pageRequ.getSize());
        } else {
            Pageable pageable = PageRequest.of(pageRequ.getPage(), pageRequ.getSize());
            medicines = drugsService.searchDrug(pageRequ.getSearch(), pageable);
        }
        List<Map<String, Object>> medicineList = medicines.getContent().stream()
                .map(med -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", med.getId());
                    map.put("addedDate", med.getAddedDate());
                    map.put("mrp", med.getMrp());
                    map.put("name", med.getName());
                    map.put("perPieceRate", med.getPerPieceRate());
                    map.put("quantity", med.getQuantity());
                    map.put("updatedDate", med.getUpdatedDate());
                    return map;
                })
                .collect(Collectors.toList());

        if (medicineList.size() > 0) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Drugs Found");
            response.put("total Page", medicines.getTotalPages());
            response.put("total Count", medicines.getTotalElements());
            response.put("data", medicineList);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(Map.of(
                "status", HttpStatus.NO_CONTENT.value(),
                "message", "No Drugs Found!!!",
                "data", Collections.emptyList(),
                "total Count", 0,
                "total Pages", 0
        ));
    }

    @PostMapping("/searchDrug")
    public ApiResponse<List<Map<String, Object>>> searchDrug(@RequestBody PageRequ pageRequ)
    {
        Pageable pageable = PageRequest.of(pageRequ.getPage(), pageRequ.getSize());
        Page<DrugsStock> medicines = drugsService.searchDrug(pageRequ.getSearch(), pageable);
        List<Map<String, Object>> medicineList = medicines.getContent().stream()
                .map(med -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", med.getId());
                    map.put("addedDate", med.getAddedDate());
                    map.put("mrp", med.getMrp());
                    map.put("name", med.getName());
                    map.put("perPieceRate", med.getPerPieceRate());
                    map.put("quantity", med.getQuantity());
                    map.put("updatedDate", med.getUpdatedDate());
                    return map;
                })
                .collect(Collectors.toList());
        if (medicineList.size()>0)
        {
            return new ApiResponse<>(medicineList, "Drugs Found!!!", HttpStatus.OK.value());
        }
        return new ApiResponse<>(null, "No Drugs Found!!!", HttpStatus.NO_CONTENT.value());
    }

}
