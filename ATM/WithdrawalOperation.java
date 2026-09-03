import java.math.BigDecimal;

public class WithdrawalOperation implements TransactionOperation {
   private final CashDispenser cashdispenser;
   private final BigDecimal amount;

   public WithdrawalOperation(CashDispenser cd, BigDecimal amount){
       this.cashdispenser = cd;
       this.amount = amount;
   }

   @Override
   public void execute(Account account){
       if (!account.debit(amount)) {
           throw new IllegalStateException("Insufficient funds for withdrawal");
       }
       cashdispenser.dispenseCash(amount);
   }
}