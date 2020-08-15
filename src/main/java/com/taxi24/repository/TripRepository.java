package com.taxi24.repository;

import com.taxi24.entity.Driver;
import com.taxi24.entity.Rider;
import com.taxi24.entity.Trip;
import com.taxi24.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findAllByTripStatus(TripStatus tripStatus);

    @Query("SELECT COUNT(t) FROM Trip AS t where t.driver=?1 AND t.rider=?2 AND t.tripStatus ='ASSIGNED' OR t.tripStatus ='ACTIVE'")
    int existByDriverAndRider(Driver driver, Rider rider);

    boolean existsByTripIdAndTripStatus(long tripId, TripStatus tripStatus);

}
