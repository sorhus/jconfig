#!/bin/bash

function fail() { echo "Installation failed."; exit; }

echo "Copying config file."
mkdir /etc/jconfig/ || fail
cp config /etc/jconfig/ || fail
source /etc/jconfig/config || fail

echo "Creating database."
echo "
DROP DATABASE IF EXISTS $DB;
DELETE FROM mysql.user WHERE User='$USER';
CREATE DATABASE $DB;
USE $DB;
CREATE TABLE configs (
    id VARCHAR(255) CHARACTER SET utf8 PRIMARY KEY, 
    json TEXT CHARACTER SET utf8 NOT NULL
);
GRANT ALL ON $DB.configs TO '$USER' IDENTIFIED BY '$PASSWORD';
" | mysql -u root -p || fail

echo "Copying executables."
cp jconfig /usr/local/bin/ || fail
cp jconfig-restapi.py /usr/local/bin/ || fail

echo "Configuring automatic upstart."
mkdir /var/log/jconfig/ || fail
echo "# Start the jconfig restapi

exec python /usr/local/bin/jconfig-restapi.py /etc/jconfig/config

start on startup
" > /etc/init/jconfig.conf || fail

service jconfig start || fail

echo "Installation complete!"

