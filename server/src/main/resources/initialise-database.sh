#!/bin/bash

source config.properties

echo "DROP DATABASE IF EXISTS $mysql_db;
DELETE FROM mysql.user WHERE User='$mysql_db_user';
CREATE DATABASE $mysql_db;
USE $mysql_db;
CREATE TABLE configs (
    \`key\` VARCHAR(255) CHARACTER SET utf8 PRIMARY KEY,
    value TEXT CHARACTER SET utf8 NOT NULL
);
GRANT ALL ON $mysql_db.configs TO '$mysql_db_user' IDENTIFIED BY '$mysql_db_password';
" | mysql -u root -p

