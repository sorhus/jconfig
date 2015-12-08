#!/bin/bash

i=$(( RANDOM % 1000 ))
while true
do 
  curl localhost:8080/api/v1/get?key=$i 2&> /dev/null
  i=$(( RANDOM % 1000 ))
done
