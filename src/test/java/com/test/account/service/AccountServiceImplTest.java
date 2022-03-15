package com.test.account.service;


import com.test.account.config.ServiceProvider;
import com.test.account.exception.InvalidRequest;
import com.test.account.model.GenericResponse;
import com.test.account.model.InputRequest;
import com.test.account.model.external.api.ApiResponse;
import com.test.account.model.external.provider.ApiProvider;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    private AccountService accountService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ResponseEntity responseEntity;

    private ServiceProvider serviceProvider;


    @Before
    public void setUp(){
        serviceProvider = new ServiceProvider();
        ApiProvider apiProvider1 = new ApiProvider("source1", "https://source1.com/v1/api/account/validate");
        ApiProvider apiProvider2 = new ApiProvider("source2", "https://source2.com/v1/api/account/validate");
        serviceProvider.setProviders(Arrays.asList(apiProvider1, apiProvider2));
        accountService = new AccountServiceImpl(restTemplate, serviceProvider);

    }

    @Test
    public void shouldThrowExceptionWhenAccountIsNotValid(){
        //given
        InputRequest inputRequest = new InputRequest();
        //don't set account null and source


        //when
       final Throwable throwable = Assertions.catchThrowable(
               ()-> accountService.validateAccount(inputRequest)
       );


        //then
        Assertions.assertThat(throwable).isInstanceOf(InvalidRequest.class);
        assertEquals(throwable.getMessage(), "Invalid Request, Account number is mandatory");
    }

    @Test
    public void shouldValidateTheAccountsWhenAllSourceReturnsTrue(){
        //given
        InputRequest inputRequest = new InputRequest();
        long accountNumber = 1234567;
        inputRequest.setAccountNumber(accountNumber);
        inputRequest.setSources(Arrays.asList("source1", "source2"));

        //when
        when(restTemplate.exchange(ArgumentMatchers.contains("https://source1.com/v1/api/account/validate"), any(HttpMethod.class), any(HttpEntity.class), (Class<Object>) any()))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(new ApiResponse(true));

        when(restTemplate.exchange(ArgumentMatchers.contains("https://source2.com/v1/api/account/validate"), any(HttpMethod.class), any(HttpEntity.class), (Class<Object>) any()))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(new ApiResponse(true));
        GenericResponse response = accountService.validateAccount(inputRequest);

        //then
        assertNotNull(response);
        assertEquals(2, response.getResult().size());
        assertEquals("source1", response.getResult().get(0).getSource());
        assertEquals("source2", response.getResult().get(1).getSource());
        assertEquals(true, response.getResult().get(0).isValid());
        assertEquals(true, response.getResult().get(1).isValid());

        assertEquals(true, response.getResult().get(0).isSuccess());
        assertEquals(true, response.getResult().get(1).isSuccess());
    }

    @Test
    public void shouldThrowExceptionWhenOneOfSourceIsNotConfigured(){
        //given
        InputRequest inputRequest = new InputRequest();
        long accountNumber = 1234567;
        inputRequest.setAccountNumber(accountNumber);

        //source 3 not configured
        inputRequest.setSources(Arrays.asList("source1", "source3"));

        //when
        when(restTemplate.exchange(ArgumentMatchers.contains("https://source1.com/v1/api/account/validate"), any(HttpMethod.class), any(HttpEntity.class), (Class<Object>) any()))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(new ApiResponse(true));

        GenericResponse response = accountService.validateAccount(inputRequest);

        //then
        assertNotNull(response);
        assertEquals(2, response.getResult().size());
        assertEquals("source1", response.getResult().get(0).getSource());
        assertEquals("source3", response.getResult().get(1).getSource());
        assertEquals(true, response.getResult().get(0).isValid());
        assertEquals(false, response.getResult().get(1).isValid());

        assertEquals(true, response.getResult().get(0).isSuccess());
        assertEquals(false, response.getResult().get(1).isSuccess());

        assertEquals("Url is not configured for source:source3", response.getResult().get(1).getErrorMessage());
    }

    @Test
    public void shouldValidateTheAccountsWhenOneOfSourceReturnsThrowsException(){
        //given
        InputRequest inputRequest = new InputRequest();
        long accountNumber = 1234567L;
        inputRequest.setAccountNumber(accountNumber);
        inputRequest.setSources(Arrays.asList("source1", "source2"));

        //when
        when(restTemplate.exchange(ArgumentMatchers.contains("https://source1.com/v1/api/account/validate"), any(HttpMethod.class), any(HttpEntity.class), (Class<Object>) any()))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(new ApiResponse(true));

        when(restTemplate.exchange(ArgumentMatchers.contains("https://source2.com/v1/api/account/validate"), any(HttpMethod.class), any(HttpEntity.class), (Class<Object>) any()))
                .thenThrow(new RuntimeException("Api not reachable"));
        when(responseEntity.getBody()).thenReturn(new ApiResponse(true));
        GenericResponse response = accountService.validateAccount(inputRequest);

        //then
        assertNotNull(response);
        assertEquals(2, response.getResult().size());
        //check source
        assertEquals("source1", response.getResult().get(0).getSource());
        assertEquals("source2", response.getResult().get(1).getSource());

        //check valid
        assertEquals(true, response.getResult().get(0).isValid());
        assertEquals(false, response.getResult().get(1).isValid());

        //check if success
        assertEquals(true, response.getResult().get(0).isSuccess());
        assertEquals(false, response.getResult().get(1).isSuccess());
        assertEquals("Error while validating accountNumber:1234567", response.getResult().get(1).getErrorMessage());
    }

    @Test
    public void shouldValidateTheAccountsWhenOneAllSourceReturnsThrowsException(){
        //given
        InputRequest inputRequest = new InputRequest();
        long accountNumber = 1234567;
        inputRequest.setAccountNumber(accountNumber);
        inputRequest.setSources(Arrays.asList("source1", "source2"));

        //when
        when(restTemplate.exchange(ArgumentMatchers.contains("https://source1.com/v1/api/account/validate"), any(HttpMethod.class), any(HttpEntity.class), (Class<Object>) any()))
                .thenThrow(new RuntimeException("Api not reachable"));

        when(restTemplate.exchange(ArgumentMatchers.contains("https://source2.com/v1/api/account/validate"), any(HttpMethod.class), any(HttpEntity.class), (Class<Object>) any()))
                .thenThrow(new RuntimeException("Api not reachable"));

        GenericResponse response = accountService.validateAccount(inputRequest);

        //then
        assertNotNull(response);
        assertEquals(2, response.getResult().size());
        //check source
        assertEquals("source1", response.getResult().get(0).getSource());
        assertEquals("source2", response.getResult().get(1).getSource());

        //check valid
        assertEquals(false, response.getResult().get(0).isValid());
        assertEquals(false, response.getResult().get(1).isValid());

        //check if success
        assertEquals(false, response.getResult().get(0).isSuccess());
        assertEquals(false, response.getResult().get(1).isSuccess());

        assertEquals("Error while validating accountNumber:1234567", response.getResult().get(0).getErrorMessage());
        assertEquals("Error while validating accountNumber:1234567", response.getResult().get(1).getErrorMessage());
    }
}