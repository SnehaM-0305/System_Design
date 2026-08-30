import java.util.*;
 
public class ChangeDispenser {
    private static final int[] NOTE_VALUES = {2000,1000,500,100} ; 
    private static final String [] NOTE_NAMES = {"TWENTY" , "TEN" , "FIVE","ONE"} ; 
    private static final int[] COIN_VALUES ={25,10,5,1} ; 
    private static final String[] COIN_NAMES = {"QUARTER","DIME","NICKEL","PENNY"}  ; 
//Calculate the denomination breakdown of the change.
    public List<String> calculateChange(int amountInCents){
        List<String> breakdown = new ArrayList<>() ; 
        int remaining = amountInCents ; 

        for(int i = 0 ; i<NOTE_VALUES.length ; i++){
            int count = remaining/NOTE_VALUES[i] ; 
            if(count>0){
                breakdown.add(count + "X"+NOTE_NAMES[i]) ; 
                remaining-=count*NOTE_VALUES[i] ; 
            }
        }

        for(int i = 0 ; i <COIN_VALUES.length ; i++){
           int count = remaining / COIN_VALUES[i];
            if (count > 0) {
                breakdown.add(count + " x " + COIN_NAMES[i]);
                remaining -= count * COIN_VALUES[i];
            } 
        }
        return breakdown ; 
    }

}
