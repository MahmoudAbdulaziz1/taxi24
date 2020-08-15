package com.taxi24.repository;

import com.taxi24.entity.Invoice;
import com.taxi24.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<com.taxi24.entity.Invoice, Long> {
    Invoice findByTrip(Trip trip);
}
