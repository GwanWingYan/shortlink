#!/usr/bin/env bash

# Configuration
BUILD_IMAGE=1
MYSQL_DIR=/tmp/shortlink/mysql
REDIS_DIR=/tmp/shortlink/redis

# 1. Build docker images
if [[ ${BUILD_IMAGE} -eq 1 ]]; then
  ./mvnw clean package || exit 1
  cd admin && docker build -t shortlink-admin . && cd .. || exit 1
  cd core && docker build -t shortlink-core . && cd .. || exit 1
  cd gateway && docker build -t shortlink-gateway . && cd .. || exit 1
  cd console && npm install && npm run build-localhost && docker build -t shortlink-console . && cd .. || exit 1
fi


# 2. Set up database persistence directories
mkdir -p ${MYSQL_DIR}
mkdir -p ${REDIS_DIR}


# 3. Start the network with docker compose
docker compose up -d


# 4. Cleanup database persistence directories
rm -rf ${MYSQL_DIR}
rm -rf ${REDIS_DIR}
