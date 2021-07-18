package com.acmebank.accountmanager.api;

import com.acmebank.accountmanager.model.Currency;
import com.acmebank.accountmanager.model.CustomerAccount;
import com.acmebank.accountmanager.model.Transfer;
import com.acmebank.accountmanager.repos.CustomerAccountRepository;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransferControllerTest {

    @Autowired
    CustomerAccountRepository customerAccountRepository;

    @LocalServerPort
    int port;

    @Test
    public void transferFromSourceToAcquireAccountSuccess() throws JSONException {

        CustomerAccount sourceCustomerAccount = new CustomerAccount(111111, Currency.HKD, getBigDecimalWithScaleFromDouble(100.00));
        customerAccountRepository.save(sourceCustomerAccount);

        CustomerAccount acquirerCustomerAccount = new CustomerAccount(111112, Currency.HKD, getBigDecimalWithScaleFromDouble(100.00));
        customerAccountRepository.save(acquirerCustomerAccount);

        Transfer transfer = new Transfer(sourceCustomerAccount.getAccountNumber(), acquirerCustomerAccount.getAccountNumber(),
                getBigDecimalWithScaleFromDouble(100.00)
        );

        given().header("Content-Type", "application/json; charset=utf-8")
                .baseUri("http://localhost")
                .basePath("/api/v1/transfer")
                .port(port)
                .body(transfer)
                .when()
                .post()
                .then()
                .statusCode(200);

        Optional<CustomerAccount> sourceAccountAfterTransfer = customerAccountRepository.findByAccountNumber(sourceCustomerAccount.getAccountNumber());
        Optional<CustomerAccount> acquirerAccountAfterTransfer = customerAccountRepository.findByAccountNumber(acquirerCustomerAccount.getAccountNumber());

        assertEquals(sourceAccountAfterTransfer.get().getBalance(), getBigDecimalWithScaleFromDouble(0.00));
        assertEquals(acquirerAccountAfterTransfer.get().getBalance(), getBigDecimalWithScaleFromDouble(200.00));
    }


    @Test
    public void transferFailsAsAcquireAccountDoesNotExist() throws JSONException {

        CustomerAccount sourceCustomerAccount = new CustomerAccount(111111, Currency.HKD, getBigDecimalWithScaleFromDouble(100.00));
        customerAccountRepository.save(sourceCustomerAccount);

        CustomerAccount acquirerCustomerAccount = new CustomerAccount(111112, Currency.HKD, getBigDecimalWithScaleFromDouble(100.00));

        Transfer transfer = new Transfer(sourceCustomerAccount.getAccountNumber(), acquirerCustomerAccount.getAccountNumber(),
                getBigDecimalWithScaleFromDouble(100.00)
        );

        given().header("Content-Type", "application/json; charset=utf-8")
                .baseUri("http://localhost")
                .basePath("/api/v1/transfer")
                .port(port)
                .body(transfer)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body("errors.acquireAccountNumber", equalTo("Account does not exist"));
    }

    @Test
    public void transferFailsAsSourceAccountDoesNotExist() throws JSONException {

        CustomerAccount sourceCustomerAccount = new CustomerAccount(111111, Currency.HKD, getBigDecimalWithScaleFromDouble(100.00));

        CustomerAccount acquirerCustomerAccount = new CustomerAccount(111112, Currency.HKD, getBigDecimalWithScaleFromDouble(100.00));
        customerAccountRepository.save(acquirerCustomerAccount);

        Transfer transfer = new Transfer(sourceCustomerAccount.getAccountNumber(), acquirerCustomerAccount.getAccountNumber(),
                getBigDecimalWithScaleFromDouble(100.00)
        );

        given().header("Content-Type", "application/json; charset=utf-8")
                .baseUri("http://localhost")
                .basePath("/api/v1/transfer")
                .port(port)
                .body(transfer)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body("errors.sourceAccountNumber", equalTo("Account does not exist"));
    }


    @Test
    public void transferFailsAsBothAccountsDontExist() throws JSONException {

        CustomerAccount sourceCustomerAccount = new CustomerAccount(111111, Currency.HKD, getBigDecimalWithScaleFromDouble(100.00));

        CustomerAccount acquirerCustomerAccount = new CustomerAccount(111112, Currency.HKD, getBigDecimalWithScaleFromDouble(100.00));

        Transfer transfer = new Transfer(sourceCustomerAccount.getAccountNumber(), acquirerCustomerAccount.getAccountNumber(),
                getBigDecimalWithScaleFromDouble(100.00)
        );

        given().header("Content-Type", "application/json; charset=utf-8")
                .baseUri("http://localhost")
                .basePath("/api/v1/transfer")
                .port(port)
                .body(transfer)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body("errors.sourceAccountNumber", equalTo("Account does not exist"))
                .body("errors.acquireAccountNumber", equalTo("Account does not exist"));
    }

    public void transferFailsAsTransferAmountIsGreaterThanAvilableFundsInSource() throws JSONException {

        CustomerAccount sourceCustomerAccount = new CustomerAccount(111111, Currency.HKD, getBigDecimalWithScaleFromDouble(100.00));

        CustomerAccount acquirerCustomerAccount = new CustomerAccount(111112, Currency.HKD, getBigDecimalWithScaleFromDouble(100.00));

        Transfer transfer = new Transfer(sourceCustomerAccount.getAccountNumber(), acquirerCustomerAccount.getAccountNumber(),
                getBigDecimalWithScaleFromDouble(101.00)
        );

        given().header("Content-Type", "application/json; charset=utf-8")
                .baseUri("http://localhost")
                .basePath("/api/v1/transfer")
                .port(port)
                .body(transfer)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body("errors.transferAmount", equalTo("Amount is greater than current source account balance"));
    }

    @Test
    public void transferFailsAsCurrencyOfAccountsDoNotMatch() throws JSONException {

        CustomerAccount sourceCustomerAccount = new CustomerAccount(111111, Currency.RMB, getBigDecimalWithScaleFromDouble(100.00));
        customerAccountRepository.save(sourceCustomerAccount);

        CustomerAccount acquirerCustomerAccount = new CustomerAccount(111112, Currency.HKD, getBigDecimalWithScaleFromDouble(100.00));
        customerAccountRepository.save(acquirerCustomerAccount);

        Transfer transfer = new Transfer(sourceCustomerAccount.getAccountNumber(), acquirerCustomerAccount.getAccountNumber(),
                getBigDecimalWithScaleFromDouble(100.00)
        );

        given().header("Content-Type", "application/json; charset=utf-8")
                .baseUri("http://localhost")
                .basePath("/api/v1/transfer")
                .port(port)
                .body(transfer)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body("errors.currency", equalTo("Currency of Accounts do not match"));
    }

    @Test
    public void transferFailsAsAmountIsZero() throws JSONException {

        CustomerAccount sourceCustomerAccount = new CustomerAccount(111111, Currency.RMB, getBigDecimalWithScaleFromDouble(100.00));
        customerAccountRepository.save(sourceCustomerAccount);

        CustomerAccount acquirerCustomerAccount = new CustomerAccount(111112, Currency.HKD, getBigDecimalWithScaleFromDouble(100.00));
        customerAccountRepository.save(acquirerCustomerAccount);

        Transfer transfer = new Transfer(sourceCustomerAccount.getAccountNumber(), acquirerCustomerAccount.getAccountNumber(),
                getBigDecimalWithScaleFromDouble(0.00)
        );

        //validation fails because transfer amount is less than 0.01
        given().header("Content-Type", "application/json; charset=utf-8")
                .baseUri("http://localhost")
                .basePath("/api/v1/transfer")
                .port(port)
                .body(transfer)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body("errors.transferAmount", equalTo("Transfer amount is less than 0.01"));
    }

    private BigDecimal getBigDecimalWithScaleFromDouble(double doubleValue){
        BigDecimal value = new BigDecimal(doubleValue);
        value = value.setScale(2, RoundingMode.HALF_UP);
        return value;

    }
}
