GRANT SELECT ON TABLE flyway_schema_history TO charondb_owner;

-- ### SCHEMAS ###

COMMENT ON SCHEMA charon_data_collection
    IS 'Schema containing data collected via web scraping';

GRANT USAGE ON SCHEMA charon_data_collection TO charon_data_collector;
GRANT ALL ON SCHEMA charon_data_collection TO charondb_owner;


-- ### TABLES ###


-- # TRANSIT ROUTE TYPE

CREATE TABLE charon_data_collection.transit_route_type
(
    id uuid,
    type character varying(16),
    PRIMARY KEY (id),
    CONSTRAINT uk_transit_route_type_type UNIQUE (type)
);

GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, TRIGGER ON TABLE charon_data_collection.transit_route_type TO charon_data_collector;

COMMENT ON TABLE charon_data_collection.transit_route_type
    IS 'Type of transit route';

COMMENT ON CONSTRAINT uk_transit_route_type_type ON charon_data_collection.transit_route_type
    IS 'Unique type code';


-- # TRANSIT ROUTE

CREATE TABLE charon_data_collection.transit_route
(
    id uuid,
    code character varying(16) NOT NULL,
    route_type_id uuid,
    PRIMARY KEY (id),
    CONSTRAINT uk_transit_route_code UNIQUE (code),
    CONSTRAINT fk_transit_route_transit_route_type FOREIGN KEY (route_type_id)
        REFERENCES charon_data_collection.transit_route_type (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, TRIGGER ON TABLE charon_data_collection.transit_route TO charon_data_collector;

COMMENT ON TABLE charon_data_collection.transit_route
    IS 'A transit route for which data will be collected';

COMMENT ON CONSTRAINT uk_transit_route_code ON charon_data_collection.transit_route
    IS 'Transit route code';

COMMENT ON CONSTRAINT fk_transit_route_transit_route_type ON charon_data_collection.transit_route
    IS 'Links route type to route';


-- # TRANSIT SCHEDULE

CREATE TABLE charon_data_collection.transit_schedule
(
    id uuid,
    collected_on timestamp NOT NULL,
    schedule_data jsonb NOT NULL,
    transit_route_id uuid,
    PRIMARY KEY (id),
    CONSTRAINT fk_transit_schedule_transit_route FOREIGN KEY (transit_route_id)
        REFERENCES charon_data_collection.transit_route (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, TRIGGER ON TABLE charon_data_collection.transit_schedule TO charon_data_collector;

COMMENT ON TABLE charon_data_collection.transit_schedule
    IS 'Expected departure and arrival times for a transit route';

COMMENT ON CONSTRAINT fk_transit_schedule_transit_route ON charon_data_collection.transit_schedule
    IS 'Links schedule to route';


-- # TRANSIT SAMPLE

CREATE TABLE charon_data_collection.transit_sample
(
    id uuid,
    location character varying(32) NOT NULL,
    sampled_on timestamp with time zone NOT NULL,
    transit_route_id uuid,
    transit_status jsonb,
    PRIMARY KEY (id),
    CONSTRAINT fk_transit_sample_transit_route FOREIGN KEY (transit_route_id)
        REFERENCES charon_data_collection.transit_route (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, TRIGGER ON TABLE charon_data_collection.transit_sample TO charon_data_collector;

COMMENT ON TABLE charon_data_collection.transit_sample
    IS 'Data sample collected indicating transit route status';

COMMENT ON CONSTRAINT fk_transit_sample_transit_route ON charon_data_collection.transit_sample
    IS 'Links route to sample';


GRANT ALL ON ALL TABLES IN SCHEMA charon_data_collection TO charondb_owner;