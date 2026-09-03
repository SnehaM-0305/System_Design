import java.math.BigDecimal;

public enum Denomination {
    FIVE_HUNDRED(new BigDecimal("500")),
    TWO_HUNDRED(new BigDecimal("200")),
    ONE_HUNDRED(new BigDecimal("100")),
    FIFTY(new BigDecimal("50"));
    
    private final BigDecimal value;

    Denomination(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}