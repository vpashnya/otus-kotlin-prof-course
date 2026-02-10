FROM ubuntu:22.04

RUN apt-get -y update && apt-get -y install nginx

COPY volumes/nginx/default.conf /etc/nginx/conf.d/
COPY volumes/nginx/nginx.conf /etc/nginx/

EXPOSE 8080/tcp

ENTRYPOINT ["/usr/sbin/nginx", "-g", "daemon off;"]