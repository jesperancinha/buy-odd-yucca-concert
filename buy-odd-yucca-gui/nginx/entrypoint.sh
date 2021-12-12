#!/bin/bash

sed -i 's/${KONG_SERVICE_IP}'"/${KONG_SERVICE_IP}/g" /etc/nginx/conf.d/default.conf

nginx -t

nginx

tail -f /dev/null
