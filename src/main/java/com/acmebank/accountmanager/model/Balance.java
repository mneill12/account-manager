package com.acmebank.accountmanager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Balance {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public BigDecimal balance;

    public Currency currency;
}
