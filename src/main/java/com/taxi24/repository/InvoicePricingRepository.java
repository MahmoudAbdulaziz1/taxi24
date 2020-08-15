package com.taxi24.repository;

import com.taxi24.entity.InvoicePricing;
import com.taxi24.enums.PricingType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InvoicePricingRepository extends JpaRepository<InvoicePricing, Long> {
    InvoicePricing findByPriceType(PricingType pricingType);
}
