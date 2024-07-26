FROM openjdk:17-alpine

RUN #sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories
RUN apk add --no-cache bash

WORKDIR /app

COPY ./target/shortlink-gateway.jar .

CMD sh -c 'java -jar shortlink-gateway.jar --spring.profiles.active=${PROFILE:-dev}'