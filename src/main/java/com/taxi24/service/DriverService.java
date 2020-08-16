package com.taxi24.service;

import com.taxi24.entity.Driver;
import com.taxi24.enums.DriverStatus;
import com.taxi24.enums.ServiceNames;
import com.taxi24.repository.DriverRepository;
import com.taxi24.util.Finals;
import com.taxi24.util.RestResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DriverService {

    @Autowired
    private RESTResponse restResponse;

    @Autowired
    private DriverRepository driverRepo;

    public RESTResponse addDriver(Driver driver){

        return RestResponseUtil.save(driverRepo, driver, driver.getDriverId(), Finals.DRIVER);
    }

    public RESTResponse getAllDrivers(){

        return RestResponseUtil.find(driverRepo, Finals.DRIVER);
    }


    public RESTResponse getDriverById(long driverId){
        return RestResponseUtil.findById(driverId, driverRepo, Finals.DRIVER);
    }

    public RESTResponse getAllAvailableDrivers(String status){

        restResponse = new RESTResponse();
        restResponse.setResponse( driverRepo.findAllByDriverStatus(DriverStatus.valueOf(status)));
        restResponse.setResp_status(200);
        restResponse.setResp_message("Success");
        restResponse.setRest_name(ServiceNames.DRIVER);
        return restResponse;
    }

    public RESTResponse getAllAvailableDriversWithin3KM(double specificPointLat, double specificPointLng){
        restResponse = new RESTResponse();
        restResponse.setResponse( driverRepo.findAllAvailableDriversWithin3KM(specificPointLat, specificPointLng));
        restResponse.setResp_status(200);
        restResponse.setResp_message("Success");
        restResponse.setRest_name(ServiceNames.DRIVER);
        return restResponse;
    }



}
