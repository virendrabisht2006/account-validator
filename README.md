# account-validator
Description : Account validator a simple spring boot microservice platform which validate account in different external validation engine.

Architectural Decision: Spring2, Junit4 log4j, rest template is used as external library. Should use maximum feature of java8.

--Assumption Taken 
1- The external service will be available. 

How it works:

Application is the Entry point of application, which has main method. It has controller POST rest API /validateAccount has input as below json
{
"accountNumber" : "1234567",
"sources": ["source1", "source2"]
} 

NOTE- some time in csv format file, if we explicitly doest not provide date format it takes dd-mm-yy. So we should format our instructionDate as dd MMM yyyy