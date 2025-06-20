package com.example.bankcards.repository;

import com.example.bankcards.entity.BankCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankCartRepository extends JpaRepository<BankCart, Long> {
}
