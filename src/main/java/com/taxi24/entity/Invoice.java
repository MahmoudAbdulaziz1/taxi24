package com.taxi24.entity;

import com.taxi24.enums.PricingCurrency;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_invoice")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private long invoiceId;

    @Column(name = "invoice_total_cost", nullable = false)
    private double priceTotalCost;


    @Column(name = "price_currency", nullable = false)
    private PricingCurrency priceCurrency;

    @OneToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @CreationTimestamp
    @Column(name = "Created_date", nullable = false, updatable= false)
    private Date createdDateTime;

    @UpdateTimestamp
    @Column(name = "last_update_date", nullable = false, updatable = true)
    private Date lastUpdateDateTime;

    @Column(nullable = false)
    private int closed = 0;

    public Invoice() {
    }

    public Invoice(double priceTotalCost, Trip trip, PricingCurrency currency) {
        this.priceTotalCost = priceTotalCost;
        this.trip = trip;
        this.priceCurrency = currency;
    }

    public Invoice(int invoiceId,  double priceTotalCost, Trip trip, PricingCurrency currency, Date createdDateTime, Date lastUpdateDateTime, int closed) {
        this( priceTotalCost, trip, currency);
        this.invoiceId = invoiceId;
        this.createdDateTime = createdDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.closed = closed;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public double getPriceTotalCost() {
        return priceTotalCost;
    }

    public void setPriceTotalCost(double priceTotalCost) {
        this.priceTotalCost = priceTotalCost;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public PricingCurrency getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(PricingCurrency priceCurrency) {
        this.priceCurrency = priceCurrency;
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
        return "Invoice [invoiceId=" + invoiceId + ", priceTotalCost" + priceTotalCost + ", trip=" + trip +
                ", priceCurrency=" + priceCurrency + "]";
    }
}
