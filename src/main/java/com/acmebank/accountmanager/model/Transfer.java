package com.acmebank.accountmanager.model;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="TRANSFER")
@Data
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @lombok.NonNull
    @org.springframework.lang.NonNull
    int sourceAccountNumber;

    @lombok.NonNull
    @org.springframework.lang.NonNull
    int acquirerAccountNumber;

    @lombok.NonNull
    @org.springframework.lang.NonNull
    BigDecimal transferAmount;

    boolean transferComplete;
}
