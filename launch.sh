#/bin/bash

CLIENT_ID=oauth-client \
CLIENT_SECRET=b544e6c3-1e15-479d-b6d9-3a7bb43bc107 \
ISSUER_URI=http://localhost:8888/auth/realms/master \
TOKEN_URI=http://localhost:8888/auth/realms/master/protocol/openid-connect/token \
AUTHORIZATION_URI=http://localhost:8888/auth/realms/master/protocol/openid-connect/auth \
USERINFO_URI=http://localhost:8888/auth/realms/master/protocol/openid-connect/userinfo \
docker-compose up