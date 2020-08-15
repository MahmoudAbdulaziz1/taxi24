package com.taxi24.entity;

import com.taxi24.enums.PricingCurrency;
import com.taxi24.enums.PricingType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_invoice_pricing")
public class InvoicePricing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private long priceId;

    @Column(name = "price_type", nullable = false)
    private PricingType pricetype;

    @Column(name = "price_cost", nullable = false)
    private double priceCost;

    @Column(name = "price_currency", nullable = false)
    private PricingCurrency priceCurrency;

    @CreationTimestamp
    @Column(name = "Created_date", nullable = false, updatable= false)
    private Date createdDateTime;

    @UpdateTimestamp
    @Column(name = "last_update_date", nullable = false, updatable = true)
    private Date lastUpdateDateTime;

    @Column(nullable = false)
    private int closed = 0;

    public InvoicePricing() {
    }

    public InvoicePricing(PricingType priceType, double priceCost, PricingCurrency priceCurrency) {
        this.pricetype = priceType;
        this.priceCost = priceCost;
        this.priceCurrency = priceCurrency;
    }

    public InvoicePricing(long priceId, PricingType priceType, double priceCost, PricingCurrency priceCurrency, Date createdDateTime, Date lastUpdateDateTime, int closed) {
        this(priceType, priceCost, priceCurrency);
        this.priceId = priceId;
        this.createdDateTime = createdDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.closed = closed;
    }

    public long getPriceId() {
        return priceId;
    }

    public void setPriceId(long priceId) {
        this.priceId = priceId;
    }

    public PricingType getPrice_type() {
        return pricetype;
    }

    public void setPrice_type(PricingType priceType) {
        this.pricetype= priceType;
    }

    public double getPriceCost() {
        return priceCost;
    }

    public void setPriceCost(double priceCost) {
        this.priceCost = priceCost;
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
        return "Pricing [priceId=" + priceId + ", priceType" + pricetype + ", priceCost=" + priceCost +
                ", priceCurrency=" + priceCurrency + "]";
    }
}
