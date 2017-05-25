package no.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vavr.collection.List;
import io.vavr.jackson.datatype.VavrModule;
import lombok.SneakyThrows;

import java.time.OffsetDateTime;

import static spark.Spark.get;

public class Main {
    public static void main(String[] args) {
        TransactionsDTO transactionsDTO = TransactionsDTO.of(OffsetDateTime.now(), List.of("sdsd", "dsds"));
        get("/transactions", (req, res) -> toJson(transactionsDTO));
    }

    @SneakyThrows
    private static String toJson(Object data) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new VavrModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(data);
    }

}