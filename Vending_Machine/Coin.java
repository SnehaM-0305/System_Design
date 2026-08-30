public enum Coin implements Denomination {

    PENNY(1) , 
    NICKEL(5),
    DIME(10),
    QUARTER(25) ; 

    private final int value ; 

    Coin(int value){
        this.value = value ; 
    }

    @Override
    public int getValue(){
        return value ; 
    }
    
}
