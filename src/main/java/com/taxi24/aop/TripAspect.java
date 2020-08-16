package com.taxi24.aop;


import com.taxi24.entity.Driver;
import com.taxi24.entity.Invoice;
import com.taxi24.entity.InvoicePricing;
import com.taxi24.entity.Trip;
import com.taxi24.enums.DriverStatus;
import com.taxi24.enums.PricingCurrency;
import com.taxi24.repository.DriverRepository;
import com.taxi24.repository.InvoicePricingRepository;
import com.taxi24.repository.InvoiceRepository;
import com.taxi24.repository.TripRepository;
import com.taxi24.util.DistanceUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Aspect
@Configuration
public class TripAspect {

    @Autowired
    private DriverRepository driverRepo;

    @Autowired
    private TripRepository tripRepo;

    @Autowired
    private InvoiceRepository invoicedRepo;

    @Autowired
    private InvoicePricingRepository pricingRepo;

    @AfterReturning(value = "execution(* com.taxi24.service.TripService.addTrip(..)) && args(trip)")
    public void afterCreatingTrip(JoinPoint joinPoint,  Trip trip) {
        Driver driver = trip.getDriver();
        if (trip.getTripStatus().name().equals("ASSIGNED") || trip.getTripStatus().name().equals("ACTIVE")){
            driver.setDriverStatus(DriverStatus.valueOf("UNAVAILABLE"));
        }else{
            driver.setDriverStatus(DriverStatus.valueOf("AVAILABLE"));
        }
        driverRepo.save(driver);
    }

    @AfterReturning(value = "execution(* com.taxi24.service.TripService.completeTrip(..)) && args(tripId)")
    public void afterCompletingTrip(JoinPoint joinPoint,  long tripId) {
        Trip trip = tripRepo.findById(tripId).get();
        Driver driver = trip.getDriver();
        if (trip.getTripStatus().name().equals("COMPLETED")){
            driver.setDriverStatus(DriverStatus.valueOf("AVAILABLE"));
        }else{
            driver.setDriverStatus(DriverStatus.valueOf("AVAILABLE"));
        }
        driverRepo.save(driver);
        measureInvoice(trip);
    }

    public void measureInvoice(Trip trip){
        List<InvoicePricing> pricing = pricingRepo.findAll();
        double distanceInKM = DistanceUtil.distance(trip.getFromLat(), trip.getToLat(), trip.getFromLng(), trip.getToLng());
        long hours = 0;
        try {
            long secs = (trip.getEndDateTime().getTime() - trip.getStartDateTime().getTime()) / 1000;
            hours = secs / 3600;
        }catch (Exception e){
            hours = 0;
        }
        double tripCost = pricing.get(0).getPriceCost() + (pricing.get(1).getPriceCost() * distanceInKM) +
                (pricing.get(2).getPriceCost() * hours);
        System.out.println(tripCost);
        invoicedRepo.save(new Invoice(tripCost, trip, PricingCurrency.EGP));
    }

}
