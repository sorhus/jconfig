#!/bin/bash

for i in $(seq 1 $1)
do
  curl -X POST localhost:8080/api/v1/set -dkey=$i -dvalue=\{\"$i\":\"$i$i\"\}
done
