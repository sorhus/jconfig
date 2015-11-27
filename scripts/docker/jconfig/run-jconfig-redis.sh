#!/bin/bash

PORT=${1:-8080}

docker run --name jconfig --link jconfig-redis:redis -d -p $PORT:8080 jconfig -b redis

