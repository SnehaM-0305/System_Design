

import java.util.Map;

public class Operator implements VendingMachineAdmin {
    private final VendingMachine machine;

    public Operator(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void addNewItem(Item item, int quantity) {
        machine.getInventory().addItem(item, quantity);
    }

    @Override
    public void restockItem(String code, int quantity) {
        machine.getInventory().restock(code, quantity);
    }

    @Override
    public int collectMoney() {
        return machine.collectMoney();
    }

    @Override
    public Map<String, Integer> getInventoryReport() {
        return machine.getInventory().getStockSnapshot();
    }
}