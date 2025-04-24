-- # TRANSIT SCHEDULE EXEC CONFIG
CREATE TABLE charon_data_collection.transit_schedule_exec_config
(
    id uuid,
    config_name character varying(64) NOT NULL,
    exec_service_class_name character varying(64) NOT NULL,
    transit_route_id uuid,
    schedule_url character varying(256) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_transit_schedule_exec_config_transit_route FOREIGN KEY (transit_route_id)
        REFERENCES charon_data_collection.transit_route (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, TRIGGER ON TABLE charon_data_collection.transit_schedule_exec_config TO charon_data_collector;
GRANT ALL ON charon_data_collection.transit_schedule_exec_config TO charondb_owner;