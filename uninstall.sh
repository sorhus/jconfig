#!/bin/bash

function fail() { echo "Uninstall failed."; exit; }

source /etc/jconfig/config || fail

echo "Removing database"
echo "
DROP DATABASE IF EXISTS $DB;
DELETE FROM mysql.user WHERE User='$USER';
" | mysql -u root -p || fail

echo "Removing binaries, configs and logs"
service jconfig stop
rm -r /etc/jconfig 
rm /usr/local/bin/jconfig /usr/local/bin/jconfig-restapi.py /etc/init/jconfig.conf /var/log/upstart/jconfig.log*

