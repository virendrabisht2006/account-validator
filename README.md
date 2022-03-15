# account-validator
Description : Account validator a simple spring boot microservice platform which validate account in different external validation engine.

Architectural Decision: Spring2, Junit4 log4j, rest template, hystrix is used as external library. Should use maximum feature of java8. Used TDD for developent for ServiceLayer.

--Assumption Taken 
1- The external service will be available. 
2- All source which will be used for validation should be passed in input request. i.e. exmaple there could 10 sources confgiured in particular env, but input request says validate agaist 2 source. the account number has to be validated against the 2 sources not with 10 source.
3- By default appication will run at 8080 port
4- if external API takes longer than expected the query will be timeout as of not default time out is 2 sec. which is configurable per env.

How it works:

Application is the Entry point of application, which has main method. Application need to be run with profile. example -Dspring.profile.active=local parameter need to be passed as args. It has controller POST rest API rest/v1/validateAccount has input as below json
{
"accountNumber" : "1234567",
"sources": ["source1", "source2"]
} 

Sources can be configured in respective env file. like application-dev.yml and application-uat.yml and respoective profile need to be passed to run the project.
