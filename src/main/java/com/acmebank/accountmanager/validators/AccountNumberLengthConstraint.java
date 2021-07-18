package com.acmebank.accountmanager.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AccountNumberLengthValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountNumberLengthConstraint {
    String message() default "Invalid Account Number length";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}