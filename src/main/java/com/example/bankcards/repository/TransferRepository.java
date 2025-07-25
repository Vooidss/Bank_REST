package com.example.bankcards.repository;

import com.example.bankcards.entity.transfer.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Page<Transfer> findAllByInitiatorId(Long initiatorId, Pageable pageable);
}
