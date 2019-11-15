# jwt
A demonstration to understand the very basics of JSON Web Tokens

## Build
### Greeting Service
`cd jwt\greeting`
`mvn package`

### Sign-On Service
`cd jwt\signon`
`mvn package`

### Demo
Nothing to do

## Run
`cd jwt`
`docker-compose up --build`

## View
The Demo application provides further explanation and test-points for the services
`GET http://localhost:7200`

## Further Testing
`GET http://localhost:7000/swagger-ui`
`GET http://localhost:7100/swagger-ui`