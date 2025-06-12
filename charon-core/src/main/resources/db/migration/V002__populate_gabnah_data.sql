INSERT INTO charon_core.transit_route_type(id, type)
VALUES(gen_random_uuid(), 'FERRY');

INSERT INTO charon_core.transit_route(id, route, route_type_id)
VALUES(gen_random_uuid(), 'GAB-NAH', (SELECT id FROM charon_core.transit_route_type WHERE type = 'FERRY'));