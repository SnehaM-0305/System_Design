import java.math.BigDecimal;

public class ATMService {

    private final AuthenticationService authService;
    private final TransactionProcessor transactionProcessor;
    private final CashDispenser cashDispenser;
    private final TransactionRepository transactionRepository;

    public ATMService(AuthenticationService authService,
                       TransactionProcessor transactionProcessor,
                       CashDispenser cashDispenser,
                       TransactionRepository transactionRepository) {
        this.authService = authService;
        this.transactionProcessor = transactionProcessor;
        this.cashDispenser = cashDispenser;
        this.transactionRepository = transactionRepository;
    }

public void withdrawCash(Account account, BigDecimal amount) {
    if (!authService.validateAccount(account)) {
        System.out.println("Authentication failed");
        return;
    }

    Transaction transaction = new Transaction(amount, TransactionType.WITHDRAWAL, account);

    TransactionOperation operation =
            TransactionOperationFactory.create(TransactionType.WITHDRAWAL, amount, cashDispenser, transactionRepository);

    try {
        operation.execute(account);
        transactionProcessor.processTransaction(transaction);
    } catch (IllegalStateException e) {
        transaction.updateStatus(TransactionStatus.FAILED);
        System.out.println("Transaction failed: " + e.getMessage());
    }

    transactionRepository.save(transaction);
}
    public void depositCash(Account account, BigDecimal amount) {
        if (!authService.validateAccount(account)) {
            System.out.println("Authentication failed");
            return;
        }

        Transaction transaction = new Transaction(amount, TransactionType.DEPOSIT, account);

        TransactionOperation operation =
                TransactionOperationFactory.create(TransactionType.DEPOSIT, amount, cashDispenser, transactionRepository);

        operation.execute(account);

        transactionProcessor.processTransaction(transaction);
        transactionRepository.save(transaction);
    }

    public void checkBalance(Account account) {
        if (!authService.validateAccount(account)) {
            System.out.println("Authentication failed");
            return;
        }

        TransactionOperation operation =
                TransactionOperationFactory.create(TransactionType.INQUIRY, null, cashDispenser, transactionRepository);

        operation.execute(account);
    }

    public void printMiniStatement(Account account) {
        if (!authService.validateAccount(account)) {
            System.out.println("Authentication failed");
            return;
        }

        TransactionOperation operation =
                TransactionOperationFactory.create(TransactionType.MINI_STATEMENT, null, cashDispenser, transactionRepository);

        operation.execute(account);
    }
}