package com.acmebank.accountmanager.model;

import com.acmebank.accountmanager.validators.MoneyFormatConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Transfer {

    @NonNull
    @NotNull
    int sourceAccountNumber;

    @NonNull
    @NotNull
    int acquirerAccountNumber;

    @NonNull
    @NotNull
    @MoneyFormatConstraint
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal transferAmount;

    @JsonIgnore
    boolean transferComplete;
}

