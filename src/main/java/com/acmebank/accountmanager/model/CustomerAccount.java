package com.acmebank.accountmanager.model;

import com.acmebank.accountmanager.validators.AccountNumberLengthConstraint;
import com.acmebank.accountmanager.validators.MoneyFormatConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table
@Data @Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class CustomerAccount {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @NotNull
    @AccountNumberLengthConstraint
    private int accountNumber;

    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency accountCurrency;

    @NonNull
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @MoneyFormatConstraint
    private BigDecimal balance;

}
