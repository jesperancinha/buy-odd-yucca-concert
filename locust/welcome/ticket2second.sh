#!/bin/bash

curl -i -X POST http://localhost:8001/services/buy-oyc-api-service/plugins \
    --data "name=rate-limiting"  \
    --data "config.second=2" \
    --data "config.hour=10000" \
    --data "config.policy=local" \
    --data "config.fault_tolerant=true" \
    --data "config.hide_client_headers=false"