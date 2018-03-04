package com.pap.nuts.modules.services.data.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.StringJoiner;

/**
 * <p>
 * This enum contains the whole table column structure, with proper data type representation.
 * </p>
 * @author Pap Gergely
 *
 */
public enum TableInsertValues {
    AGENCY("agency"){
        @Override
        public String getInsertValue(Map<String, String> records) {
            StringJoiner insertValues = new StringJoiner(",", "(",")");
            Arrays.asList("agency_id", "agency_name", "agency_url", "agency_timezone", "agency_lang", "agency_phone").forEach(e -> insertValues.add("'"+records.get(e)+"'"));

            return insertValues.toString();
        }
    },
    FEED_INFO("feed_info"){
        @Override
        public String getInsertValue(Map<String, String> records) {
            StringJoiner insertValues = new StringJoiner(",", "(",")");
            Arrays.asList("feed_publisher_name", "feed_publisher_url", "feed_lang").forEach(e -> insertValues.add("'"+records.get(e)+"'"));
            insertValues.add(strToDateFormat(records.get("feed_start_date")));
            insertValues.add(strToDateFormat(records.get("feed_end_date")));
            insertValues.add("'"+records.get("feed_version")+"'");
            insertValues.add(records.get("feed_ext_version"));

            return insertValues.toString();
        }
    },
    CALENDAR_DATES("calendar_dates"){
        @Override
        public String getInsertValue(Map<String, String> records) {
            StringJoiner insertValues = new StringJoiner(",", "(",")");
            insertValues.add("'"+records.get("service_id")+"'");
            insertValues.add(strToDateFormat(records.get("date")));
            insertValues.add(records.get("exception_type"));

            return insertValues.toString();
        }
    },
    PATHWAYS("pathways"){
        @Override
        public String getInsertValue(Map<String, String> records) {
            StringJoiner insertValues = new StringJoiner(",", "(",")");
            insertValues.add("'"+records.get("pathway_id")+"'");
            insertValues.add(records.get("pathway_type"));
            insertValues.add("'"+records.get("from_stop_id")+"'");
            insertValues.add("'"+records.get("to_stop_id")+"'");
            insertValues.add(records.get("traversal_time"));
            insertValues.add(records.get("wheelchair_traversal_time"));

            return insertValues.toString();
        }
    },
    ROUTES("routes"){
        @Override
        public String getInsertValue(Map<String, String> records) {
            StringJoiner insertValues = new StringJoiner(",", "(",")");
            insertValues.add("'"+records.get("agency_id")+"'");
            insertValues.add("'"+records.get("route_id")+"'");
            insertValues.add("'"+records.get("route_short_name")+"'");
            insertValues.add(nullValues(records.get("route_long_name"), true));
            insertValues.add(records.get("route_type"));
            insertValues.add("'"+records.get("route_desc")+"'");
            insertValues.add("'"+records.get("route_color")+"'");
            insertValues.add("'"+records.get("route_text_color")+"'");

            return insertValues.toString();
        }
    },
    SHAPES("shapes"){
        @Override
        public String getInsertValue(Map<String, String> records) {
            StringJoiner insertValues = new StringJoiner(",", "(",")");
            insertValues.add("'"+records.get("shape_id")+"'");
            insertValues.add(records.get("shape_pt_sequence"));
            insertValues.add(records.get("shape_pt_lat"));
            insertValues.add(records.get("shape_pt_lon"));
            insertValues.add(records.get("shape_dist_traveled"));

            return insertValues.toString();
        }
    },
    STOP_TIMES("stop_times"){
        @Override
        public String getInsertValue(Map<String, String> records) {
            StringJoiner insertValues = new StringJoiner(",", "(",")");
            insertValues.add("'"+records.get("trip_id")+"'");
            insertValues.add("'"+records.get("stop_id")+"'");
            insertValues.add("'"+records.get("arrival_time")+"'::interval");
            insertValues.add("'"+records.get("departure_time")+"'::interval");
            insertValues.add(records.get("stop_sequence"));
            insertValues.add(nullValues(records.get("pickup_type"), false));
            insertValues.add(nullValues(records.get("drop_off_type"), false));
            insertValues.add(records.get("shape_dist_traveled"));

            return insertValues.toString();
        }
    },
    STOPS("stops"){
        @Override
        public String getInsertValue(Map<String, String> records) {
            StringJoiner insertValues = new StringJoiner(",", "(",")");
            insertValues.add("'"+records.get("stop_id")+"'");
            insertValues.add("'"+records.get("stop_name")+"'");
            insertValues.add(records.get("stop_lat"));
            insertValues.add(records.get("stop_lon"));
            insertValues.add(nullValues(records.get("stop_code"), true));
            insertValues.add(nullValues(records.get("location_type"), false));
            insertValues.add(nullValues(records.get("parent_station"), true));
            insertValues.add(nullValues(records.get("wheelchair_boarding"), false));
            insertValues.add(nullValues(records.get("stop_direction"), false));

            return insertValues.toString();
        }
    },
    TRIPS("trips"){
        @Override
        public String getInsertValue(Map<String, String> records) {
            StringJoiner insertValues = new StringJoiner(",", "(",")");
            Arrays.asList("route_id", "trip_id", "service_id", "trip_headsign").forEach(e -> insertValues.add("'"+records.get(e)+"'"));
            insertValues.add(records.get("direction_id"));
            insertValues.add("'"+records.get("block_id")+"'");
            insertValues.add("'"+records.get("shape_id")+"'");
            insertValues.add(records.get("wheelchair_accessible"));
            insertValues.add(records.get("bikes_allowed"));

            return insertValues.toString();
        }
    }
    ;
	
	/**
	 * This function responsible for the value transformation.
	 * 
	 * @param records The map which contains the actual values.
	 * @return An insert value group
	 */
    public abstract String getInsertValue(Map<String, String> records);
    private String tableName;

    private TableInsertValues(String tableName){
        this.tableName = tableName;
    }

    public String getTableName(){
        return tableName;
    }

    private static String strToDateFormat(String rawDate){
        return "'"+rawDate.substring(0,4)+"-"+rawDate.substring(4,6)+"-"+rawDate.substring(6,8)+"'";
    }

    private static String nullValues(String value, boolean isStr){
        return "null".equals(value) ? "null" : isStr ? "'"+value+"'" : value;
    }
}
