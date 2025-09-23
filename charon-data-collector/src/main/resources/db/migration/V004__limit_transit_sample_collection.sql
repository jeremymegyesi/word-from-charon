-- Limit to 1440 samples per route. This will hold last 24 hrs sampling assuming 1 sample/min.
CREATE OR REPLACE FUNCTION enforce_sample_limit()
RETURNS TRIGGER
LANGUAGE plpgsql
SET search_path = charon_data_collection
AS $$
BEGIN
    IF (
        SELECT COUNT(*) FROM transit_sample WHERE transit_route_id = NEW.transit_route_id
    ) >= 1440 THEN
        DELETE FROM transit_sample
        WHERE id = (
            SELECT id FROM transit_sample
            WHERE category = NEW.category
            ORDER BY sampled_on ASC
            LIMIT 1
        );
    END IF;

    RETURN NEW;
END;
$$;

CREATE TRIGGER limit_route_sampling
BEFORE INSERT ON transit_sample
FOR EACH ROW
EXECUTE FUNCTION enforce_sample_limit();
