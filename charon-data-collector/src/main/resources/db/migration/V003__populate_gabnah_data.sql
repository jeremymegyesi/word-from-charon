INSERT INTO charon_data_collection.transit_route_type(id, type)
VALUES(gen_random_uuid(), 'FERRY');

INSERT INTO charon_data_collection.transit_route(id, code, route_type_id)
VALUES(gen_random_uuid(), 'GAB-NAH', (SELECT id FROM charon_data_collection.transit_route_type WHERE type = 'FERRY'));

INSERT INTO charon_data_collection.scheduled_exec_config (
    id, config_name, exec_service_class_name, utility_type, params
)
VALUES (
    gen_random_uuid(),
    'transitSchedule.gabNah',
    'BCFerriesScheduleScraperServiceImpl',
    'SCHEDULE SCRAPER',
    $$
    {
        "@class": "me.jeremymegyesi.CharonDataCollector.transitschedule.schedulescraper.ScheduleScraperParams",
        "transitRoute": "GAB-NAH",
        "scrapeUrl": "https://www.bcferries.com/routes-fares/schedules/seasonal/GAB-NAH"
    }
    $$::jsonb
);