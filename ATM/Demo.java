import java.math.BigDecimal;

public class Demo {

    public static void main(String[] args) {

        // 1. Set up the core account
        Account account = new Account("ACC1001", new BigDecimal("1000"), AccountType.SAVING_ACCOUNT);

        // 2. Set up card and user (not directly used by ATMService yet, but part of the full chain)
        Card card = new Card("CARD5001", "1234", java.time.LocalDateTime.now().plusYears(3), account);
        User user = new User("USER01", card);

        // 3. Set up shared services/collaborators
        CashDispenser cashDispenser = new CashDispenser();
        TransactionRepository transactionRepository = new TransactionRepository();
        AuthenticationService authService = new AuthenticationService();
        TransactionProcessor transactionProcessor = new TransactionProcessor();

        // 4. Wire ATMService with all its dependencies (constructor injection)
        ATMService atmService = new ATMService(authService, transactionProcessor, cashDispenser, transactionRepository);

        // 5. Set up the ATM itself
        ATM atm = new ATM("MG Road Branch", atmService);

        System.out.println("=== ATM at " + atm.getLocation() + " ===");

        // --- Authenticate first (Step 6 trace) ---
        boolean pinOk = card.validatePin("1234");
        System.out.println("PIN valid: " + pinOk);

        boolean cardValid = card.checkValidity();
        System.out.println("Card not expired: " + cardValid);

        if (!pinOk || !cardValid) {
            System.out.println("Authentication failed. Exiting.");
            return;
        }

        // --- Deposit ---
        System.out.println("\n-- Deposit 500 --");
        atmService.depositCash(account, new BigDecimal("500"));

        // --- Check balance ---
        System.out.println("\n-- Check Balance --");
        atmService.checkBalance(account);

        // --- Withdraw (should succeed, balance is now 1500) ---
        System.out.println("\n-- Withdraw 300 --");
        atmService.withdrawCash(account, new BigDecimal("300"));

        // --- Withdraw more than balance (should fail gracefully) ---
        System.out.println("\n-- Withdraw 999999 (should fail) --");
        atmService.withdrawCash(account, new BigDecimal("999999"));

        // --- Mini statement ---
        System.out.println("\n-- Mini Statement --");
        atmService.printMiniStatement(account);
    }
}