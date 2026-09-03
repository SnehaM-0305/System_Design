import java.math.BigDecimal;

public class TransactionOperationFactory {

    public static TransactionOperation create(
            TransactionType type,
            BigDecimal amount,
            CashDispenser cashDispenser,
            TransactionRepository repository) {

        switch (type) {
            case WITHDRAWAL:
                return new WithdrawalOperation(cashDispenser, amount);

            case DEPOSIT:
                return new DepositOperation(amount);

            case INQUIRY:
                return new InquiryOperation();

            case MINI_STATEMENT:
                return new MiniStatementOperation(repository);

            default:
                throw new IllegalArgumentException("Unsupported transaction type: " + type);
        }
    }
}