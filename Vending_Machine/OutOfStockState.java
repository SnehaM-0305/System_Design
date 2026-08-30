

public class OutOfStockState extends VendingMachineState {

    public OutOfStockState(VendingMachine machine) {
        super(machine);
    }

    @Override
    public void insertCoin(Coin coin) {
        throw new IllegalStateException("Machine is out of stock. Coin rejected.");
    }

    @Override
    public void insertNote(Note note) {
        throw new IllegalStateException("Machine is out of stock. Note rejected.");
    }

    @Override
    public void selectItem(String code) {
        throw new IllegalStateException("Machine is out of stock.");
    }

    @Override
    public void dispense() {
        throw new IllegalStateException("Machine is out of stock.");
    }

    @Override
    public void refund() {
        System.out.println("Refunding " + machine.getBalance() + " cents.");
        machine.resetBalance();
    }

    @Override
    public void cancel() {
        refund();
    }
}