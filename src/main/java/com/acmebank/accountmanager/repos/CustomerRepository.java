package com.acmebank.accountmanager.repos;

import com.acmebank.accountmanager.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer getByAccountNumber(Long accountNumber);
}
