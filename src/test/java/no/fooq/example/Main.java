package no.fooq.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vavr.control.Try;
import lombok.SneakyThrows;
import no.fooq.SQL;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static spark.Spark.get;

public class Main {


    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ;


        Connection conn =
                Try.of(() -> migrateDb().getDataSource())
                        .mapTry(DataSource::getConnection)
                        .onFailure(RuntimeException::new)
                        .get();

        List<Transaction> transactions =
                SQL.using(conn)
                        .select("*")
                        .from("TRANSACTION")
                        .map(Main::toTransaction)
                        .collect(toList());


        get("/transactions", (req, res) -> toJson(
                Transactions.of((OffsetDateTime.now()), transactions))
        );

    }

    @SneakyThrows
    private static Transaction toTransaction(ResultSet rs) {
        return new Transaction()
                .setId(rs.getString("ID"))
                .setAmount(rs.getDouble("AMOUNT"))
                .setCategory(rs.getString("CATEGORY"))
                .setTimestamp(toOffsetDateTime(rs));
    }

    private static OffsetDateTime toOffsetDateTime(ResultSet rs) throws SQLException {
        Instant instant = rs.getTimestamp("TIME").toInstant();
        return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
    }


    private static Flyway migrateDb() {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:h2:file:./target/example", "sa", null);
        if (flyway.migrate() == 0) {
            LOG.warn("Flyway migration was unsuccessful");
        }
        return flyway;
    }

    @SneakyThrows
    private static String toJson(Object data) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(data);
    }

}