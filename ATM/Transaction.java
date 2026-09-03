import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private final String transactionId;
    private BigDecimal amount;
    private final TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private final LocalDateTime timestamp;
    private final Account account;

    public Transaction(BigDecimal amount, TransactionType transactionType, Account acc) {
        this.transactionId = UUID.randomUUID().toString();
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionStatus = TransactionStatus.PENDING;
        this.timestamp = LocalDateTime.now();
        this.account = acc;
    }

    public TransactionStatus getStatus() {
        return transactionStatus;
    }

    public void updateStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
    public Account getAccount(){
    return account;
}
public LocalDateTime getTimestamp() {
    return timestamp;
}
public BigDecimal getAmount() {
    return amount;
}

}
