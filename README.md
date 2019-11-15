# jwt
A demonstration to understand the very basics of JSON Web Tokens

## Greeting Service
The Greeting Service provides resources, access to some of which is controlled by JWT authorisation.

#### Build
`cd jwt\greeting`

`mvn package`

## Sign-On Service
The Sign-On Service authenticates and, if authentication is successful, provides a JWT for the requester. This is very basic and just for demonstration purposes - a more likely solution is KeyCloak.

#### Build
`cd jwt\signon`

`mvn package`

### Demo Service
The Demo Service provides some explanation on the basics of JWT and provides test-points for the services.

#### Build
Nothing to do

## Run
`cd jwt`

`docker-compose up --build`

## View
The Demo Service is available from: `GET http://localhost:7200`

## Further Testing
Sign-On Service: `GET http://localhost:7000/swagger-ui`

Greeting Service: `GET http://localhost:7100/swagger-ui`
