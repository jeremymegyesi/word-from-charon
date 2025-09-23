INSERT INTO charon_core.transit_route_type(id, type)
VALUES(gen_random_uuid(), 'FERRY');

INSERT INTO charon_core.transit_route(id, code, display_name, from_location, to_location, route_type_id)
VALUES(gen_random_uuid(), 'GAB-NAH', 'Gabriola Island Ferry', 'Nanaimo', 'Gabriola Island', (SELECT id FROM charon_core.transit_route_type WHERE type = 'FERRY'));

INSERT INTO charon_core.transit_map_config(id, transit_route_id, map_type, config)
VALUES(gen_random_uuid(), (SELECT id FROM charon_core.transit_route WHERE code = 'GAB-NAH'), 'vesselfinder', '{"latitude":49.17,"longitude":-123.9,"zoom":12,"width":"100%","height":"400","names":false,"fleet":"8307055d37eec012f602eaba7f62ff03","fleetName":"gabnah"}'::jsonb);