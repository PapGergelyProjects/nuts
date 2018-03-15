CREATE OR REPLACE FUNCTION stops_within_range_and_times(center_lat DOUBLE PRECISION, center_lot DOUBLE PRECISION, radius DOUBLE PRECISION)
RETURNS TABLE(
    route_name VARCHAR(5),
    stop_name TEXT,
    stop_lat DOUBLE PRECISION,
    stop_lon DOUBLE PRECISION,
    stop_distance DOUBLE PRECISION,
    stop_color VARCHAR(6),
    text_color VARCHAR(6),
    depart_time TIME
)
AS $$
DECLARE
    center_lat ALIAS FOR $1;
    center_lot ALIAS FOR $2;
    radius ALIAS FOR $3;
BEGIN
    FOR route_name, stop_name, stop_lat, stop_lon, stop_distance, stop_color, text_color, depart_time
    IN
    (SELECT t.route_short_name, t.stop_name, t.stop_lat, t.stop_lon, t.distance, t.route_color, t.route_text_color, t.departure_time
    FROM(
        SELECT st.route_short_name, st.stop_name, st.stop_lat, st.stop_lon,
        point_in_range(center_lat, center_lot, st.stop_lat, st.stop_lon) AS distance,
        st.route_color, st.route_text_color, st."date", st.departure_time,
        ROW_NUMBER() OVER (PARTITION BY st.route_short_name, st.stop_name, st.stop_lat, st.stop_lon ORDER BY st.departure_time) AS dep_times
        FROM static_stops_with_times st
        WHERE st.departure_time >= CURRENT_TIME 
        AND st."date" = CURRENT_DATE 
        AND point_in_range(center_lat, center_lot, st.stop_lat, st.stop_lon) <= radius
    )t
    WHERE dep_times <=3)
    LOOP
        RETURN NEXT;
    END LOOP;
END
$$ LANGUAGE plpgsql^;