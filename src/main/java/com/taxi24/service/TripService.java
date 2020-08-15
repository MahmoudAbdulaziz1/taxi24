package com.taxi24.service;


import com.taxi24.entity.Driver;
import com.taxi24.entity.Rider;
import com.taxi24.entity.Trip;
import com.taxi24.enums.ServiceNames;
import com.taxi24.enums.TripStatus;
import com.taxi24.repository.TripRepository;
import com.taxi24.util.Finals;
import com.taxi24.util.RestResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TripService {

    @Autowired
    private TripRepository tripRepo;

    @Autowired
    private RESTResponse restResponse;


    public RESTResponse addTrip(Trip trip){
        boolean isTripExist = isTripExistExist(trip.getDriver(), trip.getRider());
        if(!isTripExist) {
            return RestResponseUtil.save(tripRepo, trip, trip.getTripId(), Finals.TRIP);
        }else {
            return RestResponseUtil.createTripDenied();
        }
    }

    public RESTResponse startTrip(long tripId){
        return RestResponseUtil.doTripAction(tripId, tripRepo, "ASSIGNED", "ACTIVE", Finals.TRIP);
    }

    public RESTResponse completeTrip(long tripId){
        return RestResponseUtil.doTripAction(tripId, tripRepo, "ACTIVE", "COMPLETED", Finals.TRIP);
    }

    public RESTResponse getActiveTrips(String tripStatus){
        restResponse = new RESTResponse();
        restResponse.setRest_name(ServiceNames.valueOf("TRIP"));
        restResponse.setResp_status(200);
        restResponse.setResponse(tripRepo.findAllByTripStatus(TripStatus.valueOf(tripStatus)));
        restResponse.setResp_message("Success");
        return restResponse;
    }

    private boolean isTripExistExist(Driver driver, Rider rider){
        int tripCount = tripRepo.existByDriverAndRider(driver, rider);
        return tripCount != 0;
    }
}
