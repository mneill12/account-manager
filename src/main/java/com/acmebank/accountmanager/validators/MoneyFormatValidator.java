package com.acmebank.accountmanager.validators;

import com.acmebank.accountmanager.repos.CustomerAccountRepository;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

@Configurable(autowire = Autowire.BY_TYPE, dependencyCheck = true)
public class MoneyFormatValidator implements ConstraintValidator<MoneyFormatConstraint, BigDecimal> {

    @Autowired
    CustomerAccountRepository customerAccountRepository;


    @Override
    public void initialize(MoneyFormatConstraint accountNumber) {
    }

    @Override
    public boolean isValid(BigDecimal money, ConstraintValidatorContext cxt) {
        if (money == null) {
            return true;
        }
        return Math.max(0, money.scale()) == 2;
    }
}