package com.acmebank.accountmanager.api;

import com.acmebank.accountmanager.model.Currency;
import com.acmebank.accountmanager.model.CustomerAccount;
import com.acmebank.accountmanager.repos.CustomerAccountRepository;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CustomerAccountControllerTest {

    @Before
    public void before(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @LocalServerPort
    int port;

    @Autowired
    CustomerAccountRepository customerAccountRepository;

    @Test
    public void getCustomerAccounts() throws JSONException {

        given()
                .contentType(ContentType.JSON)
                .baseUri("http://localhost")
                .basePath("/api/v1/customerAccount")
                .port(port)
                .when()
                .get()
                .then().statusCode(200);
    }


    @Test
    public void createCustomerAccountTest() throws JSONException {

        JSONObject jsonObj = new JSONObject()
                .put("accountNumber",111171)
                .put("accountCurrency","HKD")
                .put("balance", "100.00");

     given().header("Content-Type", "application/json; charset=utf-8")
             .baseUri("http://localhost")
             .basePath("/api/v1/customerAccount")
             .port(port)
             .body(jsonObj.toString())
             .when().post()
             .then().statusCode(201);
    }

    @Test
    public void createCustomerInvalidAccountNumberOutsideOf6to9Range() throws JSONException {

        //5 digits
        JSONObject customerFiveDigits = new JSONObject()
                .put("accountNumber", 11111)
                .put("accountCurrency", "HKD")
                .put("balance", "100.00");

        //10 digits
        JSONObject customerSixDigits = new JSONObject()
                .put("accountNumber", 1111111111)
                .put("accountCurrency", "HKD")
                .put("balance", "100.00");

        List<JSONObject> customerJsonObjects = Arrays.asList(customerFiveDigits, customerSixDigits);


        for (JSONObject customerJson : customerJsonObjects) {
            given().header("Content-Type", "application/json; charset=utf-8")
                    .baseUri("http://localhost")
                    .basePath("/api/v1/customerAccount")
                    .port(port)
                    .body(customerJson.toString())
                    .when().post()
                    .then().statusCode(400);
        }
    }

    @Test
    public void getCustomerAccountByAccountNumberSuccess() throws JSONException {
        BigDecimal value = new BigDecimal(100.00);
        value = value.setScale(2, RoundingMode.HALF_UP);
        CustomerAccount customerAccount = new CustomerAccount(111111,Currency.HKD, value);
        customerAccountRepository.save(customerAccount);

        String basePath = String.format("/api/v1/customerAccount/%s", customerAccount.getAccountNumber());

        given().header("Content-Type", "application/json; charset=utf-8")
                .baseUri("http://localhost")
                .basePath(basePath)
                .port(port)
                .when().get()
                .then().statusCode(200);
    }

    @Test
    public void getCustomerAccountByAccountNumberNotFound() throws JSONException {
        String basePath = String.format("/api/v1/customerAccount/%s", 111119);

        given().header("Content-Type", "application/json; charset=utf-8")
                .baseUri("http://localhost")
                .basePath(basePath)
                .port(port)
                .when().get()
                .then().statusCode(404);
    }

    @Test
    public void getCustomerBalanceByAccountNumberSuccess() throws JSONException {
        BigDecimal value = new BigDecimal(100.00);
        value = value.setScale(2, RoundingMode.HALF_UP);
        CustomerAccount customerAccount = new CustomerAccount(111111,Currency.HKD, value);
        customerAccountRepository.save(customerAccount);

        String basePath = String.format("/api/v1/customerAccount/%s/balance", customerAccount.getAccountNumber());

        given().header("Content-Type", "application/json; charset=utf-8")
                .baseUri("http://localhost")
                .basePath(basePath)
                .port(port)
                .when().get()
                .then()
                .body("currency", equalTo("HKD"))
                .body("balance",  equalTo("100.00"))
                .statusCode(200);
    }
}
