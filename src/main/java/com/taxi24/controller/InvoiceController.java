package com.taxi24.controller;

import com.taxi24.service.InvoiceService;
import com.taxi24.service.RESTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("invoice")
@CrossOrigin
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;


    @GetMapping
    public RESTResponse getAllInvoices(){
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/{invoiceId}")
    public RESTResponse getPrInvoiceById(@PathVariable long invoiceId){
        return invoiceService.getInvoiceById(invoiceId);
    }

    @GetMapping("/trip/{tripId}")
    public RESTResponse getInvoiceByTrip(@PathVariable long tripId){
        return invoiceService.getInvoiceByTrip(tripId);
    }

}
