package com.taxi24.controller;


import com.taxi24.entity.Rider;
import com.taxi24.service.RiderService;
import com.taxi24.service.RESTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("rider")
@CrossOrigin
public class RiderController {

    @Autowired
    private RiderService riderService;

    @PostMapping
    public RESTResponse addRider(@RequestBody Rider rider){
        return riderService.addRider(rider);
    }

    @GetMapping
    public RESTResponse getRiders(){
        return riderService.getRiders();
    }

    @GetMapping("/{riderId}")
    public RESTResponse getRiderById(@PathVariable  long riderId){
        return riderService.getRiderById(riderId);
    }

    @GetMapping("/closest/driver/{riderId}")
    public RESTResponse getTheThreeClosestDrivers(@PathVariable long riderId){
        return riderService.findTheThreeClosestDrivers(riderId);
    }

    @GetMapping("/closest/available/driver/{riderId}")
    public RESTResponse getTheThreeClosestAvailableDrivers(@PathVariable int riderId){
        return riderService.findTheThreeClosestAvailableDrivers(riderId);
    }

}
