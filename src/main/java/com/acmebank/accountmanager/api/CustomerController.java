package com.acmebank.accountmanager.api;

import com.acmebank.accountmanager.model.Currency;
import com.acmebank.accountmanager.model.Customer;
import com.acmebank.accountmanager.repos.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;



@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("")
    Customer getCustomerByAccountNumber(@RequestParam Long accountNumber){
        return new Customer(accountNumber, Currency.HKD, new BigDecimal(1111.11));
    }

    @PostMapping("")
    Customer newCustomer(@RequestBody Customer newCustomer){
        return customerRepository.save(newCustomer);
    }

    @GetMapping("/{customerId}")
    Customer getCustomerById(@PathVariable Long customerId){
        return customerRepository.getById(customerId);
    }

    @GetMapping("/{accountNumber}/balance")
    Customer getCustomerBalanceByAccountNumber(@PathVariable Long accountNumber){
        return customerRepository.getByAccountNumber(accountNumber);
    }
}
