#!/bin/bash

# start container
docker run -d --name mysql-jconfig -e MYSQL_ROOT_PASSWORD=$JCONFIG_MYSQL_ROOT_PASSWORD mysql:latest

MYSQL="mysql -h\$MYSQL_PORT_3306_TCP_ADDR -P\$MYSQL_PORT_3306_TCP_PORT -uroot -p$JCONFIG_MYSQL_ROOT_PASSWORD"
function booted() {
  docker run --link mysql-jconfig:mysql --rm mysql sh -c "exec printf \"select 1\" | $MYSQL" 2&> /dev/null
}

# wait until it has booted
booted
rv=$?
printf "waiting for container to boot\n"
i=0
while [ $rv -ne 0 ]
do
  i=$(( i + 1 ))
  if [ $i -gt "20" ]
  then
    printf "\n"
    printf "seems like something went wrong with mysql container\n"
    printf "please fix and retry:\n"
    docker logs mysql-jconfig
    exit 1
  else
    printf "."
    sleep 1
    booted
    rv=$?
  fi
done
printf "\n"

# create database
docker run -it --link mysql-jconfig:mysql --rm mysql sh -c "exec printf \"create database jconfig;\" | $MYSQL"

bash scripts/docker/mysql/apply-migrations.sh

