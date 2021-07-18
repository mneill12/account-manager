package com.acmebank.accountmanager.api;

import com.acmebank.accountmanager.error.ApiError;
import com.acmebank.accountmanager.model.Transfer;
import com.acmebank.accountmanager.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path="api/v1")
public class TransferController {

    @Autowired
    TransferService transferService;

    @PostMapping("/transfer")
    ResponseEntity<Object> newTransfer(@Valid @RequestBody Transfer transfer) {

        Map<String, String> errors = transferService.validateTransfer(transfer);
        if (errors.size() != 0) {
            return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST, "Validation Errors", errors), HttpStatus.BAD_REQUEST);
        } else {
            transferService.transfer(transfer);
            transfer.setTransferComplete(true);
            return new ResponseEntity<>(transfer, HttpStatus.OK);
        }
    }
}
