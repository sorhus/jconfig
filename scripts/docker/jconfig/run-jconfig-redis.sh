#!/bin/bash

PORT=${1:-8080}

docker run --link jconfig-redis:redis --rm -p$PORT:8080 jconfig -b redis
