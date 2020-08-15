package com.taxi24.repository;

import com.taxi24.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RiderRepository extends JpaRepository<Rider, Long> {
}
