## Table of Contents

- [Introduction](#Introduction)
- [Components](#components)
    * [Packer](#packer)
    * [PackerLogic](#packerlogic)    
    * [KnapsackSolver](#knapsacksolver)    
    * [KnapsackRepository](#knapsackrepository)    
    * [ProblemParser](#problemparser)    

- [HowToUse](#howtouse)    
- [Docker](#docker)

- [Build](#build)

- [WishList](#wishlist)


## Introduction

The e-commerce project is consist of mainly two services order-service and logistic-service. order-service provides
basic CRUD operations on the Order domain model, and logistic-service is simply a listener and logger of order events.

For simplicity of this assignment all these services are in the same git, but for a real project they need to be in separate git repositories.

order-service has been designed to be an **OAuth Resource Server**. Keycloak on docker has been setup as an OAuth Server. 
Also Rabbitmq and MySql are started by docker using docker-compose.
order-service uses port 8080 and Keycloak uses port 8085.

to improve maintainability and Readability of the code SOLID principals, Design patterns(DI, IOC, Domain/Service/Repository(DDD)) have been applied.

Below I describe the components that has been used in this project from a top down view. 

## Components

Single responsibility and interface segregation has been considered on designing components. So each does single job.
Also dependencies are injected using Spring framework. Data Transfer Object(DTO) pattern has been used to separate inside model from outside.
Here are the main components of order-service
 
* OrderController

    OrderController provides REST CRUD API for the order-service. In a large Microservice project there should be an API gateway instead and internal calls are done using quicker protocol like [gRPC](https://grpc.io) 
    
    Order controller provides these endpoints:
    
    POST:     /api/1/orders
    
    GET:      /api/1/orders/{id}
    
    PUT:      /api/1/orders/{id}/cancel
    
    DELETE:   /api/1/orders/{id}
          
* OrderService

    Order service is the logic of the application. It persists orders using OrderRepository and if any change occurs it raises an event using OrderEventPublisher.
    
    Order service provides create, acceptOrder(not for user but other services, should be used in Sega transaction), cancelOrder and delete functions.
    
* Order

  Order is the main domain and aggregate of the order-service project.

* OrderTo

  OrderTo is [**Data Transfer Object(DTO)**](https://en.wikipedia.org/wiki/Data_transfer_object) for the order domain model.

* OrderMapper

   maps order domains to order transfer object and vice versa.
   
* OrderRepository
    
    Is repository of order object. Does the main CRUD operations on the persistence layer.

* OrderEventPublisher
    
    Publishes events on create/acceptOrder/cancelOrder/delete operations. The DefaultOrderEventPublisher publishes events on the RabbitMQ.
    It publishes OrderCreatedEvent/OrderAcceptedEvent/OrderCanceledEvent/OrderDeleted events.

* JwtHolder

    JwtHolder provides abstraction to access to access Jwt object. DefaultJwtHolder uses Spring context to extract Jwt.

## HowToUse

To use this application make sure docker-compose is up see [here](#docker). Then use this command to generate token:
        
    curl -ss --data "grant_type=password&client_id=curl&username=reza_user&password=admin" http://localhost:8085/auth/realms/spring-security-example/protocol/openid-connect/token

The you can use this token to call APIs. for example:

    curl -X POST -H 'Content-Type: application/json' -H 'Authorization: bearer [token]' -i http://localhost:8080/api/1/orders --data '{"price":"100.23"}'
    
## Docker compose 

To start Keycloak(Auth and Identity server:) and MySql and RabbitMQ cd to docker folder and then run this command: 

    $ ./docker-compose -up

    
## Build 

Gradle is on charge for this project.
To build this project on the root of the project run this command:

    $ ./gradlew run
    
## WishList

I liked to add integration tests(API tests) for REST services in the next release. 