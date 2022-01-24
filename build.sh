#!/bin/bash

echo "Building the gateway-service"
cd api-gateway
#./mvnw clean install
mvn clean install
docker image build -t fiverr/gateway-service .
cd ..

echo "Building eureka-service"
cd eureka
#./mvnw clean install
mvn clean install
docker image build -t fiverr/eureka-server .
cd ..

echo "Building Stock Service"
cd stock-service
#./mvnw clean install
mvn clean install
docker image build -t fiverr/stock-service .
cd ..

echo "Building Exchange Service"
cd exchange-service
#./mvnw clean install
mvn clean install
docker image build -t fiverr/exchange-service .
cd ..
