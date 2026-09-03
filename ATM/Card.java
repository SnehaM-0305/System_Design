import java.time.LocalDateTime;

public class Card {
    private final String cardNumber ; 
    private final String pin ; 
    private final LocalDateTime validity ; 
    private final Account account ; 

     public Card(String cardNumber , String pin , LocalDateTime validity , Account acc){
        this.cardNumber = cardNumber ; 
        this.pin = pin ; 
        this.validity=validity;
        this.account=acc ; 
     }

    public boolean validatePin(String pinn){

        if(pinn.equals(pin)){
            return true ; 
        }

        return false ; 


    }

    public boolean checkValidity(){
         return LocalDateTime.now().isBefore(validity);
    

    
    }




}
