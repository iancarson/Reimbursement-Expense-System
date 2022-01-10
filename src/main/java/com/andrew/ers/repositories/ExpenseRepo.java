package com.andrew.ers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andrew.ers.model.Expense;

public interface ExpenseRepo extends JpaRepository<Expense, Long> {

}
