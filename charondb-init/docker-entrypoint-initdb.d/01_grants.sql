-- CHARONDB
GRANT ALL ON DATABASE charondb TO charon_flyway;
GRANT TEMPORARY, CONNECT ON DATABASE charondb TO charon_data_collector;
GRANT TEMPORARY, CONNECT ON DATABASE charondb TO charon_core;

-- FLYWAY
GRANT USAGE ON SCHEMA public TO charon_flyway;
GRANT CREATE ON SCHEMA public TO charon_flyway;