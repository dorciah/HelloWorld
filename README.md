# Net Salary App

A Spring Boot / Angular 5 application for calculating net salary in a given a day rate and country. The app makes use of the public api at http://api.nbp.pl/api/exchangerates/rates

## Setup

Required to build the software:

 - Windows (tested on Windows 10 version 1709)
 - Java (tested on 1.8u144)
 - Node JS (tested on 6.11.3 installed in C:/Programs Files/) 
 - Angular cli (tested on version 1.6.6)
 
 ## Initial Build
 
 After downloading the codebase into {install.dir} navigate to {install.dir}/runner/src/main/resources/properties and make the following changes to main.properties
 
  - base.dir
  - log.dir
  - spring.props.dir
  
  Once the changes are saved return to {install.dir}/runner and execute `mvn install` and then `runner.cmd`. Once inside runner execute the following:
  
  - `run setup/front` (this will download the node libraries and take a few minutes)
  - `run build/all+test+serve` (this includes the following steps)
    - Sets properties files based on the properties in runner
    - Runs unit tests for front and back end project modules
    - Runs integration tests
    - builds the frontend javascript code into a static folder within the backend server structure
    - builds the backend executable jar
    - starts the server
    
  Open a broswer and go to http://localhost:{back.port} - back.port is set in main.properties and is set to 4444 as standard
  
  
