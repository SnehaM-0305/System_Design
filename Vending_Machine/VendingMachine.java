

import java.util.concurrent.locks.ReentrantLock;

/**
 * Singleton context for the state machine. A single ReentrantLock guards every
 * public entry point so concurrent customers can't interleave and corrupt
 * balance/state; Inventory has its own finer-grained lock for stock counts.
 */
public class VendingMachine {
    private static volatile VendingMachine instance;

    private final Inventory inventory;
    private final ChangeDispenser changeDispenser;
    private final ReentrantLock lock = new ReentrantLock();

    private final IdleState idleState;
    private final HasMoneyState hasMoneyState;
    private final ItemSelectedState itemSelectedState;
    private final DispensingState dispensingState;
    private final OutOfStockState outOfStockState;

    private VendingMachineState currentState;
    private int balance;
    private String selectedItemCode;
    private int collectedMoney;

    private VendingMachine() {
        this.inventory = new Inventory();
        this.changeDispenser = new ChangeDispenser();
        this.idleState = new IdleState(this);
        this.hasMoneyState = new HasMoneyState(this);
        this.itemSelectedState = new ItemSelectedState(this);
        this.dispensingState = new DispensingState(this);
        this.outOfStockState = new OutOfStockState(this);
        this.currentState = idleState;
        this.balance = 0;
    }

    public static VendingMachine getInstance() {
        if (instance == null) {
            synchronized (VendingMachine.class) {
                if (instance == null) {
                    instance = new VendingMachine();
                }
            }
        }
        return instance;
    }

    public void insertCoin(Coin coin) {
        lock.lock();
        try {
            currentState.insertCoin(coin);
        } finally {
            lock.unlock();
        }
    }

    public void insertNote(Note note) {
        lock.lock();
        try {
            currentState.insertNote(note);
        } finally {
            lock.unlock();
        }
    }

    public void selectItem(String code) {
        lock.lock();
        try {
            currentState.selectItem(code);
        } finally {
            lock.unlock();
        }
    }

    public void dispenseItem() {
        lock.lock();
        try {
            currentState.dispense();
        } finally {
            lock.unlock();
        }
    }

    public void refundBalance() {
        lock.lock();
        try {
            currentState.refund();
        } finally {
            lock.unlock();
        }
    }

    public void cancelTransaction() {
        lock.lock();
        try {
            currentState.cancel();
        } finally {
            lock.unlock();
        }
    }

    public int collectMoney() {
        lock.lock();
        try {
            int amount = collectedMoney;
            collectedMoney = 0;
            return amount;
        } finally {
            lock.unlock();
        }
    }

    // ---- helpers used by state classes (package-external but intended as internal API) ----
    public void addBalance(int amount) { this.balance += amount; }
    public void resetBalance() { this.balance = 0; }
    public int getBalance() { return balance; }
    public void setSelectedItemCode(String code) { this.selectedItemCode = code; }
    public String getSelectedItemCode() { return selectedItemCode; }
    public void setState(VendingMachineState state) { this.currentState = state; }
    public VendingMachineState getCurrentState() { return currentState; }
    public void collectPayment(int amount) { this.collectedMoney += amount; }

    public Inventory getInventory() { return inventory; }
    public ChangeDispenser getChangeDispenser() { return changeDispenser; }

    public IdleState getIdleState() { return idleState; }
    public HasMoneyState getHasMoneyState() { return hasMoneyState; }
    public ItemSelectedState getItemSelectedState() { return itemSelectedState; }
    public DispensingState getDispensingState() { return dispensingState; }
    public OutOfStockState getOutOfStockState() { return outOfStockState; }
}