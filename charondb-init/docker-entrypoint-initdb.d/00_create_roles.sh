#!/bin/bash

psql -U ${POSTGRES_USER} -d ${POSTGRES_DB} <<-END
    CREATE ROLE charon_flyway WITH
        LOGIN
        NOSUPERUSER
        NOCREATEDB
        NOCREATEROLE
        INHERIT
        NOREPLICATION
        NOBYPASSRLS
        CONNECTION LIMIT -1
        PASSWORD '${POSTGRES_PASSWORD_FLYWAY}';
    COMMENT ON ROLE charon_flyway IS 'Flyway migration user for Word from Charon app';

    CREATE ROLE charon_data_collector WITH
        LOGIN
        NOSUPERUSER
        NOCREATEDB
        NOCREATEROLE
        INHERIT
        NOREPLICATION
        NOBYPASSRLS
        CONNECTION LIMIT -1
        PASSWORD '${POSTGRES_PASSWORD_CHARONDATACOLLECTOR}';
    COMMENT ON ROLE charon_data_collector IS 'Responsible for writing collected data to DB';

    CREATE ROLE charon_core WITH
        LOGIN
        NOSUPERUSER
        NOCREATEDB
        NOCREATEROLE
        INHERIT
        NOREPLICATION
        NOBYPASSRLS
        CONNECTION LIMIT -1
        PASSWORD '${POSTGRES_PASSWORD_CHARONCORE}';
    COMMENT ON ROLE charon_core IS 'Accessor of core data';
END