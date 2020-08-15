package com.taxi24.entity;

import com.taxi24.enums.DriverStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_driver")
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private long driverId;

    @Column(name = "driver_name", nullable = false)
    private String driverName;

    @Column(name = "driver_phone", nullable = false, unique = true)
    private String driverPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "driver_status", nullable = false)
    private DriverStatus driverStatus = DriverStatus.AVAILABLE;

    @Column(name = "driver_lng", nullable = false)
    private double driverLng;

    @Column(name = "driver_lat", nullable = false)
    private double driverLat;

    @CreationTimestamp
    @Column(name = "Created_date", nullable = false, updatable = false)
    private Date createdDateTime;

    @UpdateTimestamp
    @Column(name = "last_update_date", nullable = false, updatable = true)
    private Date lastUpdateDateTime;

    public Driver(){

    }

    public Driver(String driverName, String driverPhone, DriverStatus driverStatus, double driverLng, double driverLat) {
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.driverStatus = driverStatus;
        this.driverLng = driverLng;
        this.driverLat = driverLat;
    }


    public Driver(Driver driver) {
        this.driverName = driver.getDriverName();
        this.driverPhone = driver.getDriverPhone();
        this.driverStatus = driver.getDriverStatus();
        this.driverLng = driver.getDriverLng();
        this.driverLat = driver.getDriverLat();
    }

    public Driver(Driver driver, long driverId){
        this(driver);
        this.driverId = driverId;
        this.lastUpdateDateTime = new Date();
    }

    public Driver(long driverId, String driverName, String driverPhone, DriverStatus driverStatus, double driverLng, double driverLat, Date createdDateTime, Date lastUpdateDateTime) {
        this(driverName, driverPhone, driverStatus, driverLng, driverLat);
        this.driverId = driverId;
        this.createdDateTime = createdDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }

    public double getDriverLng() {
        return driverLng;
    }

    public void setDriverLng(double driverLng) {
        this.driverLng = driverLng;
    }

    public double getDriverLat() {
        return driverLat;
    }

    public void setDriverLat(double driverLat) {
        this.driverLat = driverLat;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    @Override
    public String toString() {
        return "Driver [driverId=" + driverId + ", driverName=" + driverName + ", driverPhone=" + driverPhone + ", driverStatus="
                + driverStatus + ", driverLng=" + driverLng + ", driverLat=" + driverLat + "]";
    }
}