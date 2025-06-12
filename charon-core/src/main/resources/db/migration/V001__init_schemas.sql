GRANT SELECT ON TABLE flyway_schema_history TO charondb_owner;

-- ### SCHEMAS ###

COMMENT ON SCHEMA charon_core
    IS 'Core schema for Charon';

GRANT USAGE ON SCHEMA charon_core TO charon_core;
GRANT ALL ON SCHEMA charon_core TO charondb_owner;


-- ### TABLES ###


-- # TRANSIT ROUTE TYPE

CREATE TABLE charon_core.transit_route_type
(
    id uuid,
    type character varying(16),
    PRIMARY KEY (id),
    CONSTRAINT uk_transit_route_type_type UNIQUE (type)
);

GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, TRIGGER ON TABLE charon_core.transit_route_type TO charon_core;

COMMENT ON TABLE charon_core.transit_route_type
    IS 'Type of transit route';

COMMENT ON CONSTRAINT uk_transit_route_type_type ON charon_core.transit_route_type
    IS 'Unique type code';


-- # TRANSIT ROUTE

CREATE TABLE charon_core.transit_route
(
    id uuid,
    route character varying(16) NOT NULL,
    route_type_id uuid,
    PRIMARY KEY (id),
    CONSTRAINT uk_transit_route_route UNIQUE (route),
    CONSTRAINT fk_transit_route_transit_route_type FOREIGN KEY (route_type_id)
        REFERENCES charon_core.transit_route_type (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, TRIGGER ON TABLE charon_core.transit_route TO charon_core;

COMMENT ON TABLE charon_core.transit_route
    IS 'A transit route for which data will be collected';

COMMENT ON CONSTRAINT uk_transit_route_route ON charon_core.transit_route
    IS 'Transit route code';

COMMENT ON CONSTRAINT fk_transit_route_transit_route_type ON charon_core.transit_route
    IS 'Links route type to route';


-- # TRANSIT SCHEDULE

CREATE TABLE charon_core.transit_schedule
(
    id uuid,
    collected_on timestamp NOT NULL,
    schedule_data jsonb NOT NULL,
    transit_route_id uuid,
    PRIMARY KEY (id),
    CONSTRAINT fk_transit_schedule_transit_route FOREIGN KEY (transit_route_id)
        REFERENCES charon_core.transit_route (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, TRIGGER ON TABLE charon_core.transit_schedule TO charon_core;

COMMENT ON TABLE charon_core.transit_schedule
    IS 'Expected departure and arrival times for a transit route';

COMMENT ON CONSTRAINT fk_transit_schedule_transit_route ON charon_core.transit_schedule
    IS 'Links schedule to route';


GRANT ALL ON ALL TABLES IN SCHEMA charon_core TO charondb_owner;