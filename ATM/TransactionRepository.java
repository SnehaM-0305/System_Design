import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TransactionRepository {

    private List<Transaction> transactions = new ArrayList<>();

    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactionsByAccount(Account account, int limit) {
        List<Transaction> matched = new ArrayList<>();

        for (Transaction t : transactions) {
            if (t.getAccount().equals(account)) {
                matched.add(t);
            }
        }

        matched.sort(Comparator.comparing(Transaction::getTimestamp).reversed());

        if (matched.size() > limit) {
            return matched.subList(0, limit);
        }
        return matched;
    }
}