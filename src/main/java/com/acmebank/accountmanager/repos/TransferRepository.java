package com.acmebank.accountmanager.repos;

import com.acmebank.accountmanager.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
