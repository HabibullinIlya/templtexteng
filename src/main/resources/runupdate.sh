absolutePath/liquibase --url=jdbc:postgresql://localhost:5432/testliqui \
--driver=org.postgresql.Driver \
--username=postgres --password="postgres" \
--changeLogFile=initDB.sql update