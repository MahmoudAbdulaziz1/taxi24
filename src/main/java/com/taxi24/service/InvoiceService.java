package com.taxi24.service;

import com.taxi24.entity.Trip;
import com.taxi24.enums.ServiceNames;
import com.taxi24.repository.TripRepository;
import com.taxi24.repository.InvoiceRepository;
import com.taxi24.util.Finals;
import com.taxi24.util.RestResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @Autowired
    private static RESTResponse restResponse;

    @Autowired
    private InvoiceRepository invoicedRepo;

    @Autowired
    private TripRepository tripRepo;

    public RESTResponse getAllInvoices(){
        return RestResponseUtil.find(invoicedRepo, Finals.INVOICE);
    }

    public RESTResponse getInvoiceById(long invoiceId){
        return RestResponseUtil.findById(invoiceId, invoicedRepo, Finals.INVOICE);
    }

    public RESTResponse getInvoiceByTrip(long tripId){
        restResponse = new RESTResponse();
        restResponse.setRest_name(ServiceNames.INVOICE);
        restResponse.setResp_status(200);

        if (null != Long.valueOf(tripId)) {
            if (tripRepo.existsById(tripId)) {
                Trip trip = tripRepo.findById(tripId).get();
                restResponse.setResponse(invoicedRepo.findByTrip(trip));
                restResponse.setResp_message("Success");
            } else {
                restResponse.setResp_message("Sorry! Record Not Found");
            }

        } else {
            restResponse.setResp_message("Failed");
        }
        return restResponse;
    }

}
