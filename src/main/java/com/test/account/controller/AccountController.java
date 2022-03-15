package com.test.account.controller;

import com.test.account.model.GenericResponse;
import com.test.account.model.InputRequest;
import com.test.account.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/v1")
public class AccountController {

    @Autowired
    private AccountService accountService;

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);


    @PostMapping("validateAccount")
    public GenericResponse validateAccount(@RequestBody InputRequest request){
        logger.info("Request: {} received to validate account.", request);
        GenericResponse response = accountService.validateAccount(request);
        logger.info("Request: {} completed to validate account.", request);
        return response;
    }
}
