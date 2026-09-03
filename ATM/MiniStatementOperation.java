import java.util.List;

public class MiniStatementOperation implements TransactionOperation {

    private static final int DEFAULT_LIMIT = 5;
    private final TransactionRepository repository;

    public MiniStatementOperation(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Account account) {
        List<Transaction> transactions = repository.getTransactionsByAccount(account, DEFAULT_LIMIT);

        System.out.println("---- Mini Statement ----");
        for (Transaction t : transactions) {
            System.out.println(t.getTimestamp() + " | " + t.getStatus() + " | Amount: " + t.getAmount());
        }
    }
}