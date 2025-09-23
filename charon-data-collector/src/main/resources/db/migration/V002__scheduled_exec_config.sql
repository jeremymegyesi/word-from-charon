-- # EXEC CONFIG
CREATE TABLE charon_data_collection.scheduled_exec_config
(
    id uuid,
    config_name character varying(64) NOT NULL,
    exec_service_class_name character varying(64) NOT NULL,
    utility_type character varying(64) NOT NULL,
    params jsonb NOT NULL,
    PRIMARY KEY (id)
);

GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, TRIGGER ON TABLE charon_data_collection.scheduled_exec_config TO charon_data_collector;
GRANT ALL ON charon_data_collection.scheduled_exec_config TO charondb_owner;