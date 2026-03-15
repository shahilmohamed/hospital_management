package com.project.hospitalReport.controller;

import com.project.hospitalReport.dto.InvoiceRequest;
import com.project.hospitalReport.entity.DrugInvoice;
import com.project.hospitalReport.entity.DrugsStock;
import com.project.hospitalReport.entity.InvoiceItems;
import com.project.hospitalReport.service.DrugInvoiceService;
import com.project.hospitalReport.service.DrugsService;
import com.project.hospitalReport.service.InvoiceItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    DrugsService drugsService;
    @Autowired
    DrugInvoiceService drugInvoiceService;
    @Autowired
    InvoiceItemsService invoiceItemsService;

    @PostMapping("/add")
    public ResponseEntity<?> insertInvoice(@RequestBody InvoiceRequest invoice)
    {
        try
        {
            List<InvoiceItems> items = invoice.getItems();
            List<Long> itemIds = new ArrayList<>();
            List<Integer> itemQts = new ArrayList<>();
            for (InvoiceItems i : items)
            {
                Long id = i.getId();
                Integer qty = i.getQty();
                itemIds.add(id);
                itemQts.add(qty);
            }
            List<DrugsStock> stockList = drugsService.getByIds(itemIds);
            DrugInvoice dI = new DrugInvoice();
            Double totalPrice = 0D;
            for(int i = 0; i < itemQts.size(); i++)
            {
                Integer qty = itemQts.get(i);
                Double perPieceRate = stockList.get(i).getPerPieceRate();
                Double totPrice = perPieceRate * qty;
                totalPrice += totPrice;
            }
            dI.setInvoiceAmount(totalPrice);
            dI.setBillerName(invoice.getDrugInvoice().getBillerName());
            dI.setPatientName(invoice.getDrugInvoice().getPatientName());
            dI.setInvoiceTime(LocalTime.now());
            dI.setInvoiceDate(LocalDate.now());
            String updateResult = drugsService.updateDrugs(stockList, itemQts);
            if (!updateResult.equals("Drugs Updated Successfully!!!")) {
                return ResponseEntity.ok(Map.of(
                        "status", HttpStatus.NOT_FOUND.value(),
                        "message", updateResult
                ));
            }
            DrugInvoice drugInvoice = drugInvoiceService.insertInvoice(dI);
            List<InvoiceItems> invoiceItems = new ArrayList<>();
            for(int i = 0; i < itemQts.size(); i++)
            {
                InvoiceItems items1 = new InvoiceItems();
                Integer qty = itemQts.get(i);
                Double perPieceRate = stockList.get(i).getPerPieceRate();
                Double totPrice = perPieceRate * qty;
                items1.setQty(qty);
                items1.setStock(stockList.get(i));
                items1.setTotalPrice(totPrice);
                items1.setDrugInvoice(drugInvoice);
                invoiceItems.add(items1);
            }
            List insertedItemIds = invoiceItemsService.insertItems(invoiceItems);
            if (insertedItemIds.size() != stockList.size())
            {
                return ResponseEntity.ok(Map.of(
                        "status", HttpStatus.NOT_FOUND.value(),
                        "message", "Bill Items Not Inserted Properly!!! "
                ));
            }
            return ResponseEntity.ok(Map.of(
                    "status", HttpStatus.OK.value(),
                    "message", "Bill Generated Successfully"
            ));
        } catch (Exception e) {
            System.out.println("catch");
            return ResponseEntity.ok(Map.of(
                    "status", HttpStatus.NOT_FOUND.value(),
                    "message", e.getStackTrace()
            ));
        }
    }
}
