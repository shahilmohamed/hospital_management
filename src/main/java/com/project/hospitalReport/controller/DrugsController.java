package com.project.hospitalReport.controller;

import com.project.hospitalReport.dto.ApiResponse;
import com.project.hospitalReport.entity.Appointment;
import com.project.hospitalReport.entity.DrugsStock;
import com.project.hospitalReport.service.DrugsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
@RequestMapping("/drugs")
public class DrugsController {

    @Autowired
    DrugsService drugsService;

    @PostMapping("/add")
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
                drugsService.addDrugs(d);
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
}
