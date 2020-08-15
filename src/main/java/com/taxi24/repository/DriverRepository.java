package com.taxi24.repository;

import com.taxi24.entity.Driver;
import com.taxi24.enums.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findAllByDriverStatus(DriverStatus driverStatus);

    @Query(value = "SELECT * FROM taxi24.t_driver WHERE"+
            "    (SELECT 6371 * ACOS(COS(RADIANS(?1)) * COS(RADIANS(driver_lat)) " +
            " * COS(RADIANS(driver_lng) - RADIANS(?2)) + SIN(RADIANS(?1)) " +
            " * SIN(RADIANS(driver_lat))) FROM DUAL) <= 3 "+
            "        AND driver_status LIKE 'AVAILABLE'",
            nativeQuery = true)
    List<Driver> findAllAvailableDriversWithin3KM(double specificPointLat, double specificPointLng);


    @Query(value = "SELECT  driver_id, driver_name, driver_phone, driver_lat, driver_lng, driver_status, d.created_date, d.last_update_date "+
            "FROM\n"+
            "    taxi24.t_driver d,\n"+
            "    taxi24.t_rider r\n"+
            "WHERE\n"+
            "    rider_id = ?1\n"+
            "ORDER BY (SELECT (6371 * ACOS(COS(RADIANS(r.rider_lat)) * COS(RADIANS(d.driver_lat)) * COS(RADIANS(d.driver_lng) - RADIANS(r.rider_lng)) + SIN(RADIANS(r.rider_lat)) * SIN(RADIANS(d.driver_lat)))) AS distance FROM DUAL)\n"+
            "LIMIT 0 , 3;", nativeQuery = true)
    List<Driver> findTheThreeClosestDriversForSpecificRider(long riderId);



    @Query(value = "SELECT \n"+
            "     driver_id, driver_name, driver_phone, driver_lat, driver_lng, driver_status, d.created_date, d.last_update_date "+
            "FROM\n"+
            "    taxi24.t_driver d,\n"+
            "    taxi24.t_rider r\n"+
            "WHERE\n"+
            "    rider_id = ?1 AND d.driver_status = 'AVAILABLE'"+
            " ORDER BY (SELECT (6371 * ACOS(COS(RADIANS(r.rider_lat)) * COS(RADIANS(d.driver_lat)) * COS(RADIANS(d.driver_lng) - RADIANS(r.rider_lng)) + SIN(RADIANS(r.rider_lat)) * SIN(RADIANS(d.driver_lat)))) AS distance FROM DUAL)\n "+
            "LIMIT 0 , 3;", nativeQuery = true)
    List<Driver> findTheThreeClosestAvailableDriversForSpecificRider(long riderId);

}
