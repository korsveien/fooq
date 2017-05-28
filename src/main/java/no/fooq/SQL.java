package no.fooq;

import java.sql.Connection;

public class SQL {
    public static Query using(Connection conn) {
        return Query.of(conn, new StringBuilder());
    }
}
