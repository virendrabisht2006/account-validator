package com.test.account.service;

import com.test.account.exception.InvalidRequest;
import com.test.account.model.GenericResponse;
import com.test.account.model.InputRequest;

public interface AccountService {
     GenericResponse validateAccount(InputRequest request) throws InvalidRequest;
}
