package com.acmebank.accountmanager.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MoneyFormatValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MoneyFormatConstraint {
    String message() default "Balance/Transfer amount must be to two decimal places e.g 100.00";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}