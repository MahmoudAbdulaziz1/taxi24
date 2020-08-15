package com.taxi24.service;

import com.taxi24.entity.InvoicePricing;
import com.taxi24.enums.PricingType;
import com.taxi24.enums.ServiceNames;
import com.taxi24.repository.InvoicePricingRepository;
import com.taxi24.util.Finals;
import com.taxi24.util.RestResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoicePricingService {

    @Autowired
    private static RESTResponse restResponse;

    @Autowired
    private InvoicePricingRepository pricingRepo;

    public RESTResponse addPricing(InvoicePricing pricing){
        return RestResponseUtil.save(pricingRepo, pricing, pricing.getPriceId(), Finals.PRICING);
    }

    public RESTResponse getAllPricing(){
        return RestResponseUtil.find(pricingRepo, Finals.PRICING);
    }

    public RESTResponse getPricingById(long pricingId){
        return RestResponseUtil.findById(pricingId, pricingRepo, Finals.PRICING);
    }

    public RESTResponse getPricingByType(String pricingType){
        restResponse = new RESTResponse();
        restResponse.setResponse(pricingRepo.findByPriceType(PricingType.valueOf(pricingType)));
        restResponse.setResp_status(200);
        restResponse.setResp_message("Success");
        restResponse.setRest_name(ServiceNames.PRICING);
        return restResponse;    }

}
