# Spring Cloud Gateway Versioning

## Purpose

- a demonstration of setting up a Spring Cloud Gateway that supports dynamic versioning
- submodules contain the various supporting components including
  - spring cloud config server setup
  - spring openfeign client
  - sample server code

This set of repos will demonstrate
- feign client making http requests to a Spring Cloud Gateway
- Spring Cloud Gateway receiving routing configuration from Spring Cloud Config
- ability to - at runtime - refresh the routes and pick up new version
- ability to configure routes to perform redirect

## Versioning

The mechanism of versioning is in URL.  This example exposes a simple service
`GET /api/service/version` which responds with the deployed server version (e.g. `1.0`).
When a client calls `/api/service/version` on the gateway, it responds with a `302` to the versioned URL `/api/v1/service`.
When the backend server starts up a new version of the application (`1.1`), the route is updated
via Spring Cloud Config.  Now, when a client issues `GET /api/service/version` to the gateway, 
it receives a `302 /api/v1.1/service`.  

If a client wants to continue consuming an explicit version of the api (e.g. `1.0`), the gateway
still supports direct access of `/api/v1/service`.

## Management Steps

To manage backend versions, the following steps would be performed;
1. deploy a new instance of the service side-by-side with the current version
2. update the `config/gateway-default.yml` with the additional route, changing the root URL to redirect to the latest version
3. invoke `/actuator/bus-refresh` on the gateway to pick up the new configuration


## Running

1. clone this repo
2. `git submodule init`
3. `git submodule update`
4. build the config server
   ```
   cd config-server
   gradle bootJar
   ```
5. run the config server
   `java -jar build/libs/[server].jar &`
6. build the backend server
   ```
   cd ../sample-server
   gradle bootJar
   ```
7. run two versions of the sample server
   ```
   java -Dserver.port=9001 -Dapp.version=1.1 -jar build/libs/[server].jar &
   java -jar build/libs/[server].jar &
   ```
8. build the client
   ```
   cd ../sample-client
   gradle bootJar
   ```
9. run the client
   ```
   java -jar build/libs/[server].jar &
   ```
10. test URLs
   - `GET http://localhost:8001/client/service` should respond with `1.1`
   - `GET http://localhost:8000/api/service` should respond with `1.1`
   - `GET http://localhost:8000/api/v1/service` should respond with `1.0`
   - `GET http://localhost:8000/api/v1.1/service` should respond with `1.1`
    

### RabbitMQ Docker

`docker run -d --hostname my-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management`

