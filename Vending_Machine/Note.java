public enum Note implements Denomination {
    
    ONE(100) , 
    FIVE(500) , 
    TEN(1000) , 
    TWENTY(2000) ; 

    private final int value ; 

    Note(int value){
        this.value = value ; 
    }

    @Override
    public int getValue(){
        return value ; 
    }
}
