## Table of Contents

- [Introduction](#Introduction)
- [Components](#components)
    * [OrderController](#OrderController)
    * [OrderService](#OrderService)    
    * [Order](#Order)    
    * [OrderTo](#OrderTo)    
    * [OrderMapper](#OrderMapper)    
    * [OrderRepository](#OrderRepository)    
    * [OrderEventPublisher](#OrderEventPublisher)    
    * [JwtHolder](#JwtHolder)    

- [HowToUse](#howtouse)    
- [Docker](#docker)

- [Build](#build)

- [WishList](#wishlist)


## Introduction

The e-commerce project is consist of mainly two services **order-service** and **logistic-service**. order-service provides
basic CRUD operations on the Order domain model, and logistic-service is simply a listener and logger of order events.

For simplicity of this assignment all these services are in the same git, but for a real project they need to be in separate git repositories.

order-service has been designed to be an **OAuth Resource Server**. Keycloak on docker has been setup as an OAuth Server. 
Also Rabbitmq and MySql are started by docker using docker-compose.
order-service uses port 8080 and Keycloak uses port 8085.

to improve maintainability and Readability of the code SOLID principals, Design patterns(DI, IOC, Domain/Service/Repository(DDD)) have been applied.

So to be Resilient, according to [**Reactive Manifesto**](https://www.reactivemanifesto.org/), The system should stay responsive in the face of failure.
To achieve resiliency certain attributes should be considered. Some of them could be achieved by using cloud technologies technologies for load balancing and replication(The application has been designed to be easily load balanced).
But some of them should come from design like [**Isolation**](https://www.reactivemanifesto.org/glossary#Isolation) and [**Delegation**](https://www.reactivemanifesto.org/glossary#Delegation).
Since the design of this application is Microservice the isolation is at its heart, also to achieve Delegation we should use **Asynchronous Messaging**. order-service already applied this practice by raising
its events into **topic.order_event** topic in the RabbitMQ. So in this case for example logistic-service errors is not cascaded into the order-service.

Below I describe the components that has been used in this project from a top down view. 

## Components

Single responsibility and interface segregation has been considered on designing components. So each does single job.
Also dependencies are injected using Spring framework. Data Transfer Object(DTO) pattern has been used to separate inside model from outside.
Here are the main components of order-service
 
#### * OrderController

 OrderController provides REST CRUD API for the order-service. In a large Microservice project there should be an API gateway instead and internal calls are done using quicker protocol like [gRPC](https://grpc.io) 
    
 Order controller provides these endpoints:
    
    POST:     /api/1/orders
    
    GET:      /api/1/orders/{id}
    
    PUT:      /api/1/orders/{id}/cancel
    
    DELETE:   /api/1/orders/{id}
          
#### * OrderService

Order service is the logic of the application. It persists orders using OrderRepository and if any change occurs it raises an event using OrderEventPublisher.
    
Order service provides create, acceptOrder(not for user but other services, should be used in Sega transaction), cancelOrder and delete functions.
    
#### * Order

  Order is the main domain and aggregate of the order-service project.

#### * OrderTo

  OrderTo is [**Data Transfer Object(DTO)**](https://en.wikipedia.org/wiki/Data_transfer_object) for the order domain model.

#### * OrderMapper

   maps order domains to order transfer object and vice versa.
   
#### * OrderRepository
    
   Is repository of order object. Does the main CRUD operations on the persistence layer.

#### * OrderEventPublisher
    
   Publishes events on create/acceptOrder/cancelOrder/delete operations. The DefaultOrderEventPublisher publishes events on the RabbitMQ.
   It publishes OrderCreatedEvent/OrderAcceptedEvent/OrderCanceledEvent/OrderDeleted events.

#### * JwtHolder

   JwtHolder provides abstraction to access Jwt object. DefaultJwtHolder uses Spring context to extract Jwt.

## HowToUse

To use this application make sure docker-compose is up see [here](#docker). Then use this command to generate token:
        
    curl -ss --data "grant_type=password&client_id=curl&username=reza_user&password=admin" http://localhost:8085/auth/realms/spring-security-example/protocol/openid-connect/token

The you can use this token to call APIs. for example:

    curl -X POST -H 'Content-Type: application/json' -H 'Authorization: bearer [token]' -i http://localhost:8080/api/1/orders --data '{"price":"100.23"}'
    
## Docker 

To start Keycloak(Auth and Identity server:) and MySql and RabbitMQ cd to docker folder and then run this command: 

    $ ./docker-compose -up

    
## Build 

Gradle is on charge for this project.
To build this project on the root of the project run this command:

    $ ./gradlew build
    
## WishList

* Integration test

    I liked to add integration tests(API tests) for REST services in the next release.

* Reliable events
    
    Both actions of writing on persistence and raising events should be atomic and reliable.
    
* Idempotency and multiple identical requests 

    We need to consider that client/UI may send its request multiple times(but means it really once).
    The repeating calls could happen because of any error that could happen between(like network issue) a transaction or a client/UI issue.
    So on APIs what is called duplicate action should not happen. For example we should not create multiple 
    orders with similar properties for the same user if he/she does't mean it really(We need to define the Duplication word here).
    So we would need to be [Idempotent](https://restfulapi.net/idempotent-rest-apis/) on the what is called duplicated actions. POST normally is not considered as idempotent
    but we need consider duplicate calls(or we may want to leave it to client to totally address it) 
     