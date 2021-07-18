package com.acmebank.accountmanager.repos;

import com.acmebank.accountmanager.model.CustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Integer> {
    Optional<CustomerAccount>findByAccountNumber(int accountNumber);

    Boolean existsByAccountNumber(int accountNumber);
}
