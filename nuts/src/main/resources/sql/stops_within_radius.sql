CREATE OR REPLACE FUNCTION stops_within_radius(center_lat DOUBLE PRECISION, center_lot DOUBLE PRECISION, radius DOUBLE PRECISION)
RETURNS TABLE(
    route_name VARCHAR(5),
    stop_name TEXT,
    stop_lat DOUBLE PRECISION,
    stop_lon DOUBLE PRECISION
)
AS $$
DECLARE
    center_latitude ALIAS FOR $1;
    center_longitude ALIAS FOR $2;
    radius ALIAS FOR $3;
    num CONSTANT DOUBLE PRECISION := 0.0175;
    earth_r CONSTANT DOUBLE PRECISION := 6371000;
    coord record;
BEGIN
    FOR coord IN
    (SELECT ss.route_short_name, ss.stop_name, ss.stop_lat, ss.stop_lon 
    FROM static_stops ss
    where (SELECT acos(sin(ss.stop_lat * num) * sin(center_latitude * num) + cos(ss.stop_lat * num) * cos(center_latitude * num) * cos((center_longitude * num)-(ss.stop_lon * num)))) * earth_r <= radius)
    LOOP
        route_name := coord.route_short_name;
        stop_name := coord.stop_name;
        stop_lat := coord.stop_lat;
        stop_lon := coord.stop_lon;
        RETURN NEXT;
    END LOOP;
END
$$ LANGUAGE plpgsql^;