
import java.math.BigDecimal;


public class DepositOperation implements TransactionOperation{
 private final BigDecimal amount ; 

 public DepositOperation(BigDecimal amount){
    this.amount = amount ; 
 }
    @Override
    public void execute(Account account){
        account.credit(amount) ; 
        
    }
    
}
