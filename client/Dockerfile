# build environment
FROM node:12 as build
LABEL maintainer="josphatwambugu77@gmail.com"
WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY package.json /app/package.json
RUN npm install --silent
#RUN npm install react-scripts@3.0.1 -g --silent
COPY . /app
RUN npm run build


FROM nginx:1.18.0-alpine as app

# Add bash
RUN apk add --no-cache bash

FROM debian:10.4-slim

RUN apt-get update \
    && apt-get install -y nginx-full openssl \
    && openssl req \
      -x509 \
      -subj "/C=KE/ST=NRB/L=Wambugu/O=Home/CN=pollsapp-2021.westeurope.cloudapp.azure.com" \
      -nodes \
      -days 365 \
      -newkey rsa:2048 \
      -keyout /etc/ssl/private/nginx-selfsigned.key \
      -out /etc/ssl/certs/nginx-selfsigned.crt


RUN rm -rf /usr/share/nginx/html/*
RUN rm -rf /usr/share/GeoIP/*

COPY nginx/GeoIP.dat /usr/share/GeoIP/GeoIP.dat
COPY  nginx/nginx.conf  /etc/nginx/nginx.conf
COPY --from=build /app/build /usr/share/nginx/html

EXPOSE 80 443
CMD ["/bin/bash", "-c", "nginx -g \"daemon off;\""]
