#!/bin/bash

for j in $(seq 0 $(( $1-1 )) )
do
  ./scripts/curl.sh &
done
