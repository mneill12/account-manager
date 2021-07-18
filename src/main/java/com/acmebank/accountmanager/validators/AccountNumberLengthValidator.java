package com.acmebank.accountmanager.validators;

import com.acmebank.accountmanager.repos.CustomerAccountRepository;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Configurable(autowire = Autowire.BY_TYPE, dependencyCheck = true)
public class AccountNumberLengthValidator implements ConstraintValidator<AccountNumberLengthConstraint, Integer> {

    @Autowired
    CustomerAccountRepository customerAccountRepository;


    @Override
    public void initialize(AccountNumberLengthConstraint accountNumber) {
    }

    @Override
    public boolean isValid(Integer accountNumber, ConstraintValidatorContext cxt) {
        if (accountNumber == null) {
            return true;
        }
        return accountNumber.toString().length() >=6 && accountNumber.toString().length() <=9;
    }
}