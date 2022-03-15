package com.test.account.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.test.account.config.ServiceProvider;
import com.test.account.exception.InvalidRequest;
import com.test.account.model.ResponseResult;
import com.test.account.model.external.provider.ApiProvider;
import com.test.account.model.GenericResponse;
import com.test.account.model.InputRequest;
import com.test.account.model.external.api.ApiRequest;
import com.test.account.model.external.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private RestTemplate restTemplate;

    private ServiceProvider serviceProvider;



    @Autowired
    public AccountServiceImpl(final RestTemplate restTemplate, final ServiceProvider serviceProvider){
        this.restTemplate = restTemplate;
        this.serviceProvider = serviceProvider;
    }

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    @HystrixCommand(fallbackMethod = "queryTimeOutHandler", commandKey = "queryTimeOutHandler")
    public GenericResponse validateAccount(InputRequest request) throws InvalidRequest {
        logger.info("About to validate the incoming request: {}", request);
        validateRequest(request);
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setAccountNumber(request.getAccountNumber());
        HttpEntity<ApiRequest> entity = buildRequest(apiRequest);

        Map<String, String> sourceToUrlMapping =  serviceProvider.getProviders()
                .stream().collect(Collectors.toMap(ApiProvider::getName, ApiProvider::getUrl));

      List<ResponseResult> results =  request.getSources().stream().map(s->{

            ResponseResult responseResult = new ResponseResult();

            try{
                if(sourceToUrlMapping.containsKey(s)) {
                    String url  = sourceToUrlMapping.get(s);
                    logger.debug("About to call external service api url: {} for accountNumber: {}", url, apiRequest.getAccountNumber());
                    ApiResponse response = (ApiResponse) restTemplate.
                            exchange(url, HttpMethod.POST, entity, Object.class).getBody();

                    logger.debug("Successfully got response from service api url: {} for accountNumber: {}", url, apiRequest.getAccountNumber());
                    responseResult.setValid(response.isValid());
                    responseResult.setSuccess(true);
                } else {
                    String message = "Url is not configured for source:" +  s;
                    logger.error(message);
                    responseResult.setSuccess(false);
                    responseResult.setErrorMessage(message);
                }
                responseResult.setSource(s);
            } catch (Exception e){
                String message = "Error while validating accountNumber:" + apiRequest.getAccountNumber();
                logger.error(message);
                responseResult.setSuccess(false);
                responseResult.setErrorMessage(message);
                responseResult.setSource(s);
            }
            return responseResult;
        }).collect(Collectors.toList());

        return new GenericResponse(results);
    }

    private void validateRequest(InputRequest request){
        if(Objects.nonNull(request) &&  0L == request.getAccountNumber()){
            String message ="Invalid Request, Account number is mandatory";
            logger.error(message);
            throw new InvalidRequest(message);
        }
    }

    private HttpEntity<ApiRequest> buildRequest(ApiRequest request){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(request, headers);
    }

    private GenericResponse queryTimeOutHandler(InputRequest request, Throwable t){
        String message = "Query taking longer than expected time of 2 sec";
        logger.warn(message, t.getMessage());
        ResponseResult result = new ResponseResult();
        result.setSuccess(false);
        result.setErrorMessage(message);
        return new GenericResponse(Arrays.asList(result));
    }
}
