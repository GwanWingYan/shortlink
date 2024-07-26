#!/usr/bin/env bash

# 1. Build backend docker images
./mvnw clean package || exit 1
cd admin && docker build -t shortlink-admin . && cd .. || exit 1
cd core && docker build -t shortlink-core . && cd .. || exit 1
cd gateway && docker build -t shortlink-gateway . && cd .. || exit 1

# 2. Build frontend docker image
cd console && npm install && npm run build-localhost && docker build -t shortlink-console . && cd .. || exit 1