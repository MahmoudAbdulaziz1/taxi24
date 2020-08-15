package com.taxi24.controller;

import com.taxi24.entity.InvoicePricing;
import com.taxi24.service.InvoicePricingService;
import com.taxi24.service.RESTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pricing")
@CrossOrigin
public class InvoicePricingController {

    @Autowired
    private InvoicePricingService pricingService;

    @PostMapping
    public RESTResponse addPricing(@RequestBody InvoicePricing pricing){
        return pricingService.addPricing(pricing);
    }

    @GetMapping
    public RESTResponse getAllPricing(){
        return pricingService.getAllPricing();
    }

    @GetMapping("/{priceId}")
    public RESTResponse getPriceById(@PathVariable  long priceId){
        return pricingService.getPricingById(priceId);
    }

    @GetMapping("/type/{priceType}")
    public RESTResponse getPriceByType(@PathVariable String priceType){
        return pricingService.getPricingByType(priceType);
    }

}
