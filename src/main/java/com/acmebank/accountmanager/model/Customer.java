package com.acmebank.accountmanager.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table
@Data @Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @org.springframework.lang.NonNull
    @NonNull private Long accountNumber;

    @Enumerated(EnumType.STRING)
    @org.springframework.lang.NonNull
    @NonNull private Currency accountCurrency;

    @org.springframework.lang.NonNull
    @NonNull private BigDecimal balance;

}
