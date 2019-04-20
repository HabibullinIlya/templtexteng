#!/usr/bin/env bash
/home/ilya/liquibase-3.6.3-bin --url=jdbc:postgresql://localhost:5432/testliqui \
--driver=org.postgresql.Driver \
--username=testdb --password="root" \
--changeLogFile=initDB.sql update