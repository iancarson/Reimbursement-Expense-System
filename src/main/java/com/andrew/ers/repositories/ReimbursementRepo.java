package com.andrew.ers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andrew.ers.model.Reimbursement;

public interface ReimbursementRepo extends JpaRepository<Reimbursement, Long> {
}
