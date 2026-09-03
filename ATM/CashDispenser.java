import java.math.BigDecimal;
import java.util.*;

public class CashDispenser {

    private final Map<Denomination, Integer> cashAvailable;

    public CashDispenser() {
        cashAvailable = new HashMap<>();

        cashAvailable.put(Denomination.FIVE_HUNDRED, 10);
        cashAvailable.put(Denomination.TWO_HUNDRED, 10);
        cashAvailable.put(Denomination.ONE_HUNDRED, 20);
        cashAvailable.put(Denomination.FIFTY, 20);
    }

    public synchronized Map<Denomination, Integer> dispenseCash(BigDecimal amount) {

        Map<Denomination, Integer> dispensed = new HashMap<>();

        // Largest denomination first
        List<Denomination> denominations =
                new ArrayList<>(cashAvailable.keySet());

        denominations.sort(
            Comparator.comparing(Denomination::getValue).reversed()
        );

        BigDecimal remaining = amount;

        for (Denomination denomination : denominations) {

            BigDecimal value = denomination.getValue();
            int available = cashAvailable.get(denomination);

            int required = remaining
                    .divideToIntegralValue(value)
                    .intValue();

            int toDispense = Math.min(required, available);

            if (toDispense > 0) {
                dispensed.put(denomination, toDispense);

                remaining = remaining.subtract(
                        value.multiply(BigDecimal.valueOf(toDispense))
                );
            }
        }

        // Couldn't make exact amount
        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException(
                "Cannot dispense exact amount"
            );
        }

        // Actually remove the notes from dispenser
        for (Map.Entry<Denomination, Integer> entry : dispensed.entrySet()) {
            Denomination denomination = entry.getKey();
            int count = entry.getValue();

            cashAvailable.put(
                denomination,
                cashAvailable.get(denomination) - count
            );
        }

        return dispensed;
    }
}