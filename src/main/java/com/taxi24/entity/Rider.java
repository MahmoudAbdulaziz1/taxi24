package com.taxi24.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_rider")
public class Rider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rider_id")
    private long riderId;

    @Column(name = "rider_name", nullable = false)
    private String riderName;

    @Column(name = "rider_phone", nullable = false, unique = true)
    private String riderPhone;

    @Column(name = "rider_lng")
    private double riderLng;

    @Column(name = "rider_lat")
    private double riderLat;

    @CreationTimestamp
    @Column(name = "Created_date", nullable = false, updatable= false)
    private Date createdDateTime;

    @UpdateTimestamp
    @Column(name = "last_update_date", nullable = false, updatable = true)
    private Date lastUpdateDateTime;

    @Column(nullable = false)
    private int closed = 0;

    public Rider() {
    }

    public Rider(String riderName, String riderPhone, double riderLng, double riderLat) {
        this.riderName = riderName;
        this.riderPhone = riderPhone;
        this.riderLng = riderLng;
        this.riderLat = riderLat;
    }

    public Rider(int riderId, String riderName, String riderPhone, double riderLng, double riderLat, Date createdDateTime, Date lastUpdateDateTime, int closed) {
        this(riderName, riderPhone, riderLng, riderLat);
        this.riderId = riderId;
        this.createdDateTime = createdDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.closed = closed;
    }

    public long getRiderId() {
        return riderId;
    }

    public void setRiderId(long riderId) {
        this.riderId = riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderPhone() {
        return riderPhone;
    }

    public void setRiderPhone(String riderPhone) {
        this.riderPhone = riderPhone;
    }

    public double getRiderLng() {
        return riderLng;
    }

    public void setRiderLng(double riderLng) {
        this.riderLng = riderLng;
    }

    public double getRiderLat() {
        return riderLat;
    }

    public void setRiderLat(double riderLat) {
        this.riderLat = riderLat;
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

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    @Override
    public String toString() {
        return "Rider [riderId=" + riderId + ", riderName=" + riderName + ", riderPhone=" + riderPhone +
                ", riderLng=" + riderLng + ", riderLat=" + riderLat + "]";
    }
}
