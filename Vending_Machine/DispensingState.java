

import java.util.List;

public class DispensingState extends VendingMachineState {

    public DispensingState(VendingMachine machine) {
        super(machine);
    }

    @Override
    public void insertCoin(Coin coin) {
        throw new IllegalStateException("Cannot insert coin while dispensing.");
    }

    @Override
    public void insertNote(Note note) {
        throw new IllegalStateException("Cannot insert note while dispensing.");
    }

    @Override
    public void selectItem(String code) {
        throw new IllegalStateException("Already dispensing.");
    }

    @Override
    public void dispense() {
        String code = machine.getSelectedItemCode();
        Item item = machine.getInventory().getItem(code);

        machine.getInventory().reduceStock(code);
        System.out.println("Dispensing: " + item.getName());

        int change = machine.getBalance() - item.getPrice();
        if (change > 0) {
            List<String> breakdown = machine.getChangeDispenser().calculateChange(change);
            System.out.println("Returning change (" + change + " cents): " + breakdown);
        }

        machine.collectPayment(item.getPrice());
        machine.resetBalance();
        machine.setSelectedItemCode(null);
        machine.setState(machine.getIdleState());
    }

    @Override
    public void refund() {
        throw new IllegalStateException("Cannot refund while dispensing.");
    }

    @Override
    public void cancel() {
        throw new IllegalStateException("Cannot cancel while dispensing.");
    }
}