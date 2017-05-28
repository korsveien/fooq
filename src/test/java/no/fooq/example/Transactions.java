package no.fooq.example;

import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Value(staticConstructor = "of")
class Transactions {
    OffsetDateTime id;
    List<Transaction> transactions;
}
