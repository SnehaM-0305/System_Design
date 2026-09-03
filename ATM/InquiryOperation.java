import java.math.BigDecimal;

public class InquiryOperation implements TransactionOperation {
    @Override
    public void execute(Account account){
      BigDecimal balance =   account.getBalance() ; 
      System.out.println("Your balance is " + balance);
    }
    
}
