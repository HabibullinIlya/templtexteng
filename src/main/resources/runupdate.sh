#!/usr/bin/env bash
/home/ilya/liquibase-3.6.3-bin --url=jdbc:postgresql://192.168.99.100:30080/testliqui2 \
--driver=org.postgresql.Driver \
--username=testdb --password="root" \ad
--changeLogFile=initDB.sql update