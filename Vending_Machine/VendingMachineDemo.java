

public class VendingMachineDemo {
    public static void main(String[] args) {
        VendingMachine machine = VendingMachine.getInstance();
        Operator operator = new Operator(machine);

        // Admin stocks the machine
        operator.addNewItem(new Item("A1", "Coke", 150), 2);
        operator.addNewItem(new Item("A2", "Chips", 100), 1);

        // 1) Successful purchase with change
        machine.insertNote(Note.ONE);
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        machine.selectItem("A1");
        machine.dispenseItem();

        System.out.println("---");

        // 2) Insufficient funds
        try {
            machine.insertCoin(Coin.DIME);
            machine.selectItem("A2");
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
            machine.refundBalance();
        }

        System.out.println("---");

        // 3) Buy the last A2, then trigger out-of-stock on the next attempt
        machine.insertNote(Note.ONE);
        machine.selectItem("A2");
        machine.dispenseItem();

        try {
            machine.insertNote(Note.ONE);
            machine.selectItem("A2");
        } catch (OutOfStockExceptions e) {
            System.out.println("Error: " + e.getMessage());
            machine.refundBalance();
        }

        System.out.println("---");
        System.out.println("Operator collected: " + operator.collectMoney() + " cents");
        System.out.println("Inventory report: " + operator.getInventoryReport());
    }
}