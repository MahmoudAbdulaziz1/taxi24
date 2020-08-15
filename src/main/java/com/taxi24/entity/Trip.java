package com.taxi24.entity;

import com.taxi24.enums.TripStatus;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_trip")
public class Trip implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private long tripId;

    @Enumerated(EnumType.STRING)
    @Column(name = "trip_status")
    private TripStatus tripStatus;

    @Column(name = "from_lng")
    private double fromLng;

    @Column(name = "from_lat")
    private double fromLat;

    @Column(name = "to_lng")
    private double toLng;

    @Column(name = "to_lat")
    private double toLat;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "rider_id")
    private Rider rider;

    @CreationTimestamp
    @Column(name = "Created_date", nullable = false, updatable = false)
    private Date createdDateTime;

    @Column(name = "start_date", nullable = true)
    private Date startDateTime;

    @Column(name = "end_date", nullable = true)
    private Date endDateTime;

    public Trip() {
    }

    public Trip(TripStatus tripStatus, double fromLng, double fromLat, double toLng, double toLat, Driver driver, Rider rider) {
        this.tripStatus = tripStatus;
        this.fromLng = fromLng;
        this.fromLat = fromLat;
        this.toLng = toLng;
        this.toLat = toLat;
        this.driver = driver;
        this.rider = rider;
    }

    public Trip(int tripId, TripStatus tripStatus, double fromLng, double fromLat, double toLng, double toLat, Driver driver, Rider rider, Date createdDateTime) {
        this(tripStatus, fromLng, fromLat, toLng, toLat, driver, rider);
        this.tripId = tripId;
        this.createdDateTime = createdDateTime;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }

    public double getFromLng() {
        return fromLng;
    }

    public void setFromLng(double fromLng) {
        this.fromLng = fromLng;
    }

    public double getFromLat() {
        return fromLat;
    }

    public void setFromLat(double fromLat) {
        this.fromLat = fromLat;
    }

    public double getToLng() {
        return toLng;
    }

    public void setToLng(double toLng) {
        this.toLng = toLng;
    }

    public double getToLat() {
        return toLat;
    }

    public void setToLat(double toLat) {
        this.toLat = toLat;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Override
    public String toString() {
        return "Trip [tripId=" + tripId + ", driverName=" + driver.getDriverName() + ", riderName=" + rider.getRiderId() +
                ", fromLng=" + fromLng + ", fromLat=" + fromLat + ", toLng=" + toLng + ", toLat=" + toLat;
    }
}
