

public class HasMoneyState extends VendingMachineState {

    public HasMoneyState(VendingMachine machine) {
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
        if (!machine.getInventory().isAvailable(code)) {
            throw new OutOfStockExceptions("Item " + code + " is out of stock.");
        }
        Item item = machine.getInventory().getItem(code);
        if (machine.getBalance() < item.getPrice()) {
            throw new InsufficientFundsException(
                "Insufficient funds. Need " + (item.getPrice() - machine.getBalance()) + " more cents.");
        }
        machine.setSelectedItemCode(code);
        machine.setState(machine.getItemSelectedState());
    }

    @Override
    public void dispense() {
        throw new IllegalStateException("Select an item first.");
    }

    @Override
    public void refund() {
        System.out.println("Refunding " + machine.getBalance() + " cents.");
        machine.resetBalance();
        machine.setState(machine.getIdleState());
    }

    @Override
    public void cancel() {
        refund();
    }
}