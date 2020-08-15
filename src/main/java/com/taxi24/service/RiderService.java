package com.taxi24.service;

import com.taxi24.entity.Rider;
import com.taxi24.enums.ServiceNames;
import com.taxi24.repository.DriverRepository;
import com.taxi24.repository.RiderRepository;
import com.taxi24.util.Finals;
import com.taxi24.util.RestResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RiderService {

    @Autowired
    private static RESTResponse restResponse;

    @Autowired
    private RiderRepository riderRepo;

    @Autowired
    private DriverRepository driverRepo;

    public RESTResponse addRider(Rider rider){
        return RestResponseUtil.save(riderRepo, rider, rider.getRiderId(), Finals.RIDER);
    }

    public RESTResponse getRiders(){
        return RestResponseUtil.find(riderRepo, Finals.RIDER);
    }

    public RESTResponse getRiderById(long riderId){
        return RestResponseUtil.findById(riderId, riderRepo, Finals.RIDER);
    }
    public RESTResponse findTheThreeClosestDrivers(long riderId){

        restResponse = new RESTResponse();
        restResponse.setRest_name(ServiceNames.RIDER);
        restResponse.setResp_status(200);

        if (null != Long.valueOf(riderId)) {
            if (riderRepo.existsById(riderId)) {
                restResponse.setResponse(driverRepo.findTheThreeClosestDriversForSpecificRider(riderId));
                restResponse.setResp_message("Success");
            } else {
                restResponse.setResp_message("Sorry! Rider Record Not Found");
            }

        } else {
            restResponse.setResp_message("Failed");
        }
        return restResponse;
    }

    public RESTResponse findTheThreeClosestAvailableDrivers(long riderId){


        restResponse = new RESTResponse();
        restResponse.setRest_name(ServiceNames.RIDER);
        restResponse.setResp_status(200);

        if (null != Long.valueOf(riderId)) {
            if (riderRepo.existsById(riderId)) {
                restResponse.setResponse(driverRepo.findTheThreeClosestAvailableDriversForSpecificRider(riderId));
                restResponse.setResp_message("Success");
            } else {
                restResponse.setResp_message("Sorry! Rider Record Not Found");
            }

        } else {
            restResponse.setResp_message("Failed");
        }
        return restResponse;
    }



}
