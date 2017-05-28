package no.fooq;

import lombok.SneakyThrows;
import lombok.Value;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@Value(staticConstructor = "of")
public class Query {
    Connection connection;
    StringBuilder stringBuilder;
    private final String SEPARATOR = ", ";

    public Query select(String... columnNames) {
        Stream.of(columnNames)
//                    .intersperse(SEPARATOR)
                .forEach(stringBuilder::append);
        return this;
    }

    public Query from(String... tableNames) {
        Stream.of(tableNames)
//                    .intersperse(SEPARATOR)
                .forEach(stringBuilder::append);
        return this;
    }

    public Optional<ResultSet> fetch() {
        Optional<ResultSet> maybeResult;
        try {
            maybeResult = Optional.of(connection.prepareStatement(stringBuilder.toString()).executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return maybeResult;
    }

    @SneakyThrows
    public <T> Stream<T> map(Function<ResultSet, T> mapper) {
        ResultSet rs = this.fetch().orElseThrow(RuntimeException::new);
        return new ResultSetIterable<>(rs, mapper).stream();
    }

}
