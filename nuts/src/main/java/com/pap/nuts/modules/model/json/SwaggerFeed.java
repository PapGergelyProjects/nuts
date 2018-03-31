package com.pap.nuts.modules.model.json;

import java.util.Arrays;

/**
 * This class represents the getFeed json.
 * Every property is public, because no need to instantiate, this is only for gson parser.
 * 
 * @author Pap Gergely
 *
 */
public class SwaggerFeed {

    public String status;
    public long ts;
    public Results results;

    public class Results{
        public int total;
        public int limit;
        public int page;
        public int numPages;
        public Feeds[] feeds;

        @Override
        public String toString() {
            return "{" + "total=" + total + ", limit=" + limit + ", page=" + page + ", numPages=" + numPages + ", feeds=" + Arrays.toString(feeds) + '}';
        }
    }

    public class Feeds{
        public String id;
        public String ty;
        public String t;
        public Location l;
        public FeedURL u;
        public Latest latest;

        @Override
        public String toString() {
            return "{" + "id=" + id + ", ty=" + ty + ", t=" + t + ", l=" + l + ", u=" + u + ", latest=" + latest + '}';
        }
    }

    public class Location{
        public long id;
        public long pid;
        public String t;
        public String n;
        public double lat;
        public double lng;

        @Override
        public String toString() {
            return "{" + "id=" + id + ", pid=" + pid + ", t=" + t + ", n=" + n + ", lat=" + lat + ", lon=" + lng + '}';
        }
    }

    public class FeedURL{
        public String d;

        @Override
        public String toString() {
            return "{" + "d=" + d + '}';
        }
    }

    public class Latest{
        public long ts;

        @Override
        public String toString() {
            return "{" + "ts=" + ts + '}';
        }
        
    }

    @Override
    public String toString() {
        return "SwaggerFeed{" + "status=" + status + ", ts=" + ts + ", results=" + results + '}';
    }
    
}
