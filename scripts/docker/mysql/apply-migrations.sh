#!/bin/bash

cd server/src/main/resources/sql/migration 
MYSQL_HOST=$(docker run --rm --link mysql-jconfig:mysql mysql sh -c 'exec echo "$MYSQL_PORT_3306_TCP_ADDR"')
docker run --rm --link mysql-jconfig:mysql -e JCONFIG_MYSQL_ROOT_PASSWORD -v $(pwd):/flyway/sql shouldbee/flyway -user=root -password=$JCONFIG_MYSQL_ROOT_PASSWORD -url=jdbc:mysql://$MYSQL_HOST/jconfig migrate

