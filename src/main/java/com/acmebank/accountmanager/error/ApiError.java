package com.acmebank.accountmanager.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ApiError {

    private HttpStatus status;
    private String message;
    private Map<String, String> errors;

    public ApiError(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        this.errors = new HashMap<String, String>(){{
            put("error", error);

        }};
    }

    public ApiError(HttpStatus status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
}
