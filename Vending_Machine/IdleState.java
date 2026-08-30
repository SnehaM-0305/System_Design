

public class IdleState extends VendingMachineState {

    public IdleState(VendingMachine machine) {
        super(machine);
    }

    @Override
    public void insertCoin(Coin coin) {
        machine.addBalance(coin.getValue());
        machine.setState(machine.getHasMoneyState());
    }

    @Override
    public void insertNote(Note note) {
        machine.addBalance(note.getValue());
        machine.setState(machine.getHasMoneyState());
    }

    @Override
    public void selectItem(String code) {
        throw new IllegalStateException("Insert money before selecting an item.");
    }

    @Override
    public void dispense() {
        throw new IllegalStateException("No item selected.");
    }

    @Override
    public void refund() {
        System.out.println("Nothing to refund.");
    }

    @Override
    public void cancel() {
        System.out.println("Nothing to cancel.");
    }
}