### Example

```
        List<Transaction> transactions =
                SQL.using(conn)
                        .select("*")
                        .from("TRANSACTION")
                        .map(Main::toTransaction)
                        .collect(toList());

```