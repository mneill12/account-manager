package com.acmebank.accountmanager.api;

import com.acmebank.accountmanager.model.Transfer;
import com.acmebank.accountmanager.repos.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="api/v1")
public class TransferController {

    @Autowired
    TransferRepository transferRepository;

    @PostMapping("/transfer")
    Transfer newTransfer(@RequestBody Transfer transfer) {
       return transferRepository.save(transfer);
    }
}
