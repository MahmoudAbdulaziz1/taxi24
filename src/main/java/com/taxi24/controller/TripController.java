package com.taxi24.controller;

import com.taxi24.service.TripService;
import com.taxi24.entity.Trip;
import com.taxi24.service.RESTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("trip")
@CrossOrigin
public class TripController {

    @Autowired
    private TripService tripService;

    @PostMapping
    public RESTResponse addRider(@RequestBody Trip trip){
        return tripService.addTrip(trip);
    }

    @GetMapping("/start/{tripId}")
    public RESTResponse startTrip(@PathVariable long tripId){
        return tripService.startTrip(tripId);
    }

    @GetMapping("/complete/{tripId}")
    public RESTResponse completeTrip(@PathVariable long tripId){
        return tripService.completeTrip(tripId);
    }

    @GetMapping("/active")
    public RESTResponse getActiveTrip(){
        return tripService.getActiveTrips("ACTIVE");
    }

}
