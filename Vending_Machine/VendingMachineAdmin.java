

import java.util.Map;

public interface VendingMachineAdmin {
    void addNewItem(Item item, int quantity);
    void restockItem(String code, int quantity);
    int collectMoney();
    Map<String, Integer> getInventoryReport();
}