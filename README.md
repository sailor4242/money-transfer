## Money Transfer

Implementation of a RESTful API for creating accounts and money transfers between accounts.

Run app by `build_run.sh` or make jar by yourself.
Running needs port 8080 exposed.
Http server starts on localhost.

**Packaging**:

    mvn clean package
    
**Running**:
   
    java -jar target/transfer-0.0.1-SNAPSHOT.jar

**API**:

**Get account**:

    GET localhost:8080/account/{id}

**Create account**: 

    POST localhost:8080/account/
    
**Add money**:

    PUT localhost:8080/account/{id}/add/{amount}
    
**Withdraw money**:

    PUT localhost:8080/account/{id}/withdraw/{amount}

**Money transfer**:

    PATCH localhost:8080/account/{fromId}/transfer/{toId}/{amount}