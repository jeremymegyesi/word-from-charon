INSERT INTO charon_core.transit_route_type(id, type)
VALUES(gen_random_uuid(), 'FERRY');

INSERT INTO charon_core.transit_route(id, code, display_name, from_location, to_location, route_type_id)
VALUES(gen_random_uuid(), 'GAB-NAH', 'Gabriola Island Ferry', 'Nanaimo', 'Gabriola Island', (SELECT id FROM charon_core.transit_route_type WHERE type = 'FERRY'));