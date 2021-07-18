package com.acmebank.accountmanager.api;

import com.acmebank.accountmanager.model.Balance;
import com.acmebank.accountmanager.model.CustomerAccount;
import com.acmebank.accountmanager.repos.CustomerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/customerAccount")
public class CustomerAccountController {

    @Autowired
    CustomerAccountRepository customerAccountRepository;

    @GetMapping("")
    ResponseEntity<Object> getAllCustomerAccounts(){
        List<CustomerAccount> customers = customerAccountRepository.findAll();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{accountNumber}")
    ResponseEntity<Object> getCustomerByAccountNumber(@PathVariable int accountNumber){

        Optional<CustomerAccount> customerAccountOptional = customerAccountRepository.findByAccountNumber(accountNumber);
        return customerAccountOptional.isPresent()
                ? new ResponseEntity<>(customerAccountOptional.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/{accountNumber}/balance")
    ResponseEntity<Object> getCustomerBalanceByAccountNumber(@PathVariable int accountNumber) {
        Optional<CustomerAccount> customerAccountOptional = customerAccountRepository.findByAccountNumber(accountNumber);
        return customerAccountOptional.isPresent()
                ? new ResponseEntity<>(new Balance(customerAccountOptional.get().getBalance(), customerAccountOptional.get().getAccountCurrency()), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    ResponseEntity newCustomer(@Valid @RequestBody CustomerAccount newCustomerAccount){
            if(customerAccountRepository.existsByAccountNumber(newCustomerAccount.getAccountNumber())){
                return new ResponseEntity<>("Account Number exists", HttpStatus.BAD_REQUEST);
            }
            CustomerAccount customerAccount = customerAccountRepository.save(newCustomerAccount);
            return new ResponseEntity<>(customerAccount, HttpStatus.CREATED);
    }

}
