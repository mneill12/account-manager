package com.acmebank.accountmanager.services;

import com.acmebank.accountmanager.model.CustomerAccount;
import com.acmebank.accountmanager.model.Transfer;
import com.acmebank.accountmanager.repos.CustomerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TransferService {

    @Autowired
    CustomerAccountRepository customerAccountRepository;

    public Map<String, String> validateTransfer(Transfer transfer){

        Map<String, String> errors = new HashMap<>();

        Optional<CustomerAccount> sourceAccountOptional = customerAccountRepository.findByAccountNumber(transfer.getSourceAccountNumber());
        if(sourceAccountOptional.isEmpty()){
          errors.put("sourceAccountNumber", "Account does not exist");
        }

        Optional<CustomerAccount> acquireAccountOptional = customerAccountRepository.findByAccountNumber(transfer.getAcquirerAccountNumber());
        if(acquireAccountOptional.isEmpty()){
            errors.put("acquireAccountNumber", "Account does not exist");
        }

        if(sourceAccountOptional.isPresent() && acquireAccountOptional.isPresent()){
            if(transfer.getTransferAmount().compareTo(sourceAccountOptional.get().getBalance()) > 0){
                errors.put("transferAmount", "Amount is greater than current source account balance");
            }

            if(sourceAccountOptional.get().getAccountCurrency() != acquireAccountOptional.get().getAccountCurrency()){
                errors.put("currency", "Currency of Accounts do not match");
            }

            if(transfer.getTransferAmount().compareTo(BigDecimal.valueOf(0.01)) < 0){
                errors.put("transferAmount", "Transfer amount is less than 0.01");
            }
        }
        return errors;
    };

    public void transfer(Transfer transfer){
        CustomerAccount sourceCustomerAccount = customerAccountRepository.findByAccountNumber(transfer.getSourceAccountNumber())
                .get();

        CustomerAccount acquirerCustomerAccount = customerAccountRepository.findByAccountNumber(transfer.getAcquirerAccountNumber())
                .get();

        sourceCustomerAccount.setBalance(sourceCustomerAccount
                .getBalance()
                .subtract(transfer.getTransferAmount())
        );

        acquirerCustomerAccount.setBalance(
                acquirerCustomerAccount.getBalance().add(
                        transfer.getTransferAmount()
                )
        );
        customerAccountRepository.save(sourceCustomerAccount);
        customerAccountRepository.save(acquirerCustomerAccount);
    }
}
