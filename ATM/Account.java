import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Account {
    private final String accountNumber;
    private BigDecimal balance;
    private final AccountType accountType;
    private final LocalDateTime openedOn;

    public Account(String accountNumber, BigDecimal balance, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.openedOn = LocalDateTime.now();
    }

    public boolean debit(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            System.out.println("Not enough balance");
            return false;
        }
        balance = balance.subtract(amount);
        System.out.println("Money debited: " + amount + " | Total balance: " + balance);
        return true ; 
    }

    public void credit(BigDecimal amount) {
        balance = balance.add(amount);
        System.out.println("Money credited: " + amount + " | Total balance: " + balance);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountNumber.equals(account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}