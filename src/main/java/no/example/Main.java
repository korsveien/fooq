package no.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vavr.collection.List;

import java.time.OffsetDateTime;

import static spark.Spark.get;

public class Main {
    public static void main(String[] args) {
        TransactionsDTO transactionsDTO = TransactionsDTO.of(OffsetDateTime.now(), List.of("sdsd", "Sdsd"));

        Gson gson = new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter()).create();

        get("/transactions", (req, res) -> gson.toJson(transactionsDTO));
    }

}