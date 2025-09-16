INSERT INTO charon_data_collection.transit_route_type(id, type)
VALUES(gen_random_uuid(), 'FERRY');

INSERT INTO charon_data_collection.transit_route(id, code, route_type_id)
VALUES(gen_random_uuid(), 'GAB-NAH', (SELECT id FROM charon_data_collection.transit_route_type WHERE type = 'FERRY'));

INSERT INTO charon_data_collection.transit_schedule_exec_config (
    id, config_name, exec_service_class_name, transit_route_id, schedule_url
)
VALUES (
    gen_random_uuid(),
    'transitSchedule.gabNah',
    'BCFerriesScheduleScraperServiceImpl',
    (SELECT id FROM charon_data_collection.transit_route WHERE code = 'GAB-NAH'),
    'https://www.bcferries.com/routes-fares/schedules/seasonal/GAB-NAH'
);