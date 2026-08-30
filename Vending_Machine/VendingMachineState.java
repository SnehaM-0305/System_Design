public abstract class VendingMachineState {
    protected final VendingMachine machine ; 
    protected VendingMachineState(VendingMachine m){
        this.machine = m ; 
    }

    public abstract void insertCoin(Coin coin) ; 
    public abstract void insertNote(Note note) ; 
     public abstract void selectItem(String code);
    public abstract void dispense();
    public abstract void refund();
    public abstract void cancel();
}
