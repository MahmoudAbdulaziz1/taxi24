package com.taxi24.controller;

import com.taxi24.entity.Driver;
import com.taxi24.service.DriverService;
import com.taxi24.service.RESTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("driver")
@CrossOrigin
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping
    public RESTResponse addDriver(@RequestBody Driver driver){
        return driverService.addDriver(driver);
    }

    @GetMapping
    public RESTResponse getAllDrivers(){
        return driverService.getAllDrivers();
    }

    @GetMapping("/status/{driverStatus}")
    public RESTResponse getAllAvailableDrivers(@PathVariable String driverStatus){
        return driverService.getAllAvailableDrivers(driverStatus);
    }

    @GetMapping("/{driverId}")
    public RESTResponse getDriverById(@PathVariable long driverId){
        return driverService.getDriverById(driverId);
    }

    @GetMapping("/{specificPointLat}/{specificPointLng}")
    public RESTResponse getAllAvailableDriversWithin3KM(@PathVariable float specificPointLat, @PathVariable float specificPointLng){
        return driverService.getAllAvailableDriversWithin3KM(specificPointLat, specificPointLng);
    }
}
