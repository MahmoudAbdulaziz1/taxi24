package com.taxi24.util;

import com.taxi24.entity.Trip;
import com.taxi24.enums.ServiceNames;
import com.taxi24.enums.TripStatus;
import com.taxi24.repository.TripRepository;
import com.taxi24.service.RESTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public class RestResponseUtil {

    @Autowired
    private static RESTResponse restResponse;

    public static RESTResponse save(JpaRepository repo, Object object, long objectId, String serviceName){
        restResponse = new RESTResponse();
        restResponse.setRest_name(ServiceNames.valueOf(serviceName));
        restResponse.setResp_status(200);

        if (null != object) {
            if (repo.existsById(objectId)) {
                restResponse.setResponse(repo.save(object));
                restResponse.setResp_message("Updated Successfully");
            } else {
                restResponse.setResponse(repo.save(object));
                restResponse.setResp_message("Saved Successfully");
            }

        } else {
            restResponse.setResp_message("Failed");
        }
        return restResponse;
    }

    public static RESTResponse find(JpaRepository repo, String serviceName){

        restResponse = new RESTResponse();
        restResponse.setResponse(repo.findAll());
        restResponse.setResp_status(200);
        restResponse.setResp_message("Success");
        restResponse.setRest_name(ServiceNames.valueOf(serviceName));
        return restResponse;
    }

    public static RESTResponse findById(long objectId, JpaRepository repo, String serviceName){
        restResponse = new RESTResponse();
        restResponse.setRest_name(ServiceNames.valueOf(serviceName));
        restResponse.setResp_status(200);

        if (null != Long.valueOf(objectId)) {
            if (repo.existsById(objectId)) {
                restResponse.setResponse(repo.findById(objectId).get());
                restResponse.setResp_message("Success");
            } else {
                restResponse.setResp_message("Sorry! Record Not Found");
            }

        } else {
            restResponse.setResp_message("Failed");
        }
        return restResponse;
    }

    public static RESTResponse createTripDenied(){
        restResponse = new RESTResponse();
        restResponse.setRest_name(ServiceNames.valueOf("TRIP"));
        restResponse.setResp_status(200);
        restResponse.setResp_message("Sorry! Trip Already Created And Not Completed Yet");
        return restResponse;
    }

    public static RESTResponse doTripAction(long objectId, TripRepository repo, String currentTripStatus,
                                            String tripStatus, String serviceName){


        restResponse = new RESTResponse();
        restResponse.setRest_name(ServiceNames.valueOf(serviceName));
        restResponse.setResp_status(200);

        if (null != Long.valueOf(objectId)) {
            if (repo.existsByTripIdAndTripStatus(objectId, TripStatus.valueOf(currentTripStatus))) {

                Trip trip = repo.findById(objectId).get();
                trip.setTripStatus(TripStatus.valueOf(tripStatus));
                if(currentTripStatus.equals("ASSIGNED"))
                    trip.setStartDateTime(new Date());
                else
                    trip.setEndDateTime(new Date());
                restResponse.setResponse(repo.save(trip));
                restResponse.setResp_message("Success");
            } else {
                restResponse.setResp_message("Sorry! THis Trip With This Status Not Exist ");
            }

        } else {
            restResponse.setResp_message("Failed");
        }
        return restResponse;

    }
}
