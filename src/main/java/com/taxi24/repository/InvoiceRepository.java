package com.taxi24.repository;

import com.taxi24.entity.Trip;
import com.taxi24.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Invoice findByTrip(Trip trip);
}
