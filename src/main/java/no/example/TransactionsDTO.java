package no.example;

import io.vavr.collection.List;
import lombok.Value;

import java.time.OffsetDateTime;

@Value(staticConstructor = "of")
public class TransactionsDTO {
    OffsetDateTime id;
    List<String> data;
}
