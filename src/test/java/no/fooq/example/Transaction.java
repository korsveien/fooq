package no.fooq.example;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Data
@Accessors(chain = true)
public class Transaction {
    String id;
    Double amount;
    OffsetDateTime timestamp;
    String category;
}
