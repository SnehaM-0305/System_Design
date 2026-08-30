

public class ItemSelectedState extends VendingMachineState {

    public ItemSelectedState(VendingMachine machine) {
        super(machine);
    }

    @Override
    public void insertCoin(Coin coin) {
        machine.addBalance(coin.getValue());
    }

    @Override
    public void insertNote(Note note) {
        machine.addBalance(note.getValue());
    }

    @Override
    public void selectItem(String code) {
        throw new IllegalStateException("Item already selected. Dispense or cancel first.");
    }

    @Override
    public void dispense() {
        machine.setState(machine.getDispensingState());
        machine.getCurrentState().dispense();
    }

    @Override
    public void refund() {
        System.out.println("Refunding " + machine.getBalance() + " cents.");
        machine.resetBalance();
        machine.setSelectedItemCode(null);
        machine.setState(machine.getIdleState());
    }

    @Override
    public void cancel() {
        refund();
    }
}