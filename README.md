# oauth-keycloak

docker run -p 8888:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin quay.io/keycloak/keycloak:16.1.0
[Login](http://localhost:8888) - admin / admin

## Create a client
 - Client ID: oauth-client
 - Access Type: confidential
 - Valid redirect URIs: *

## Get the client secret
After save the Credentials tab will apper. Let's switch to that tab and you will find the generated client secret. Update that value in the launch.sh or on the application.yaml file of the gateway service. 
