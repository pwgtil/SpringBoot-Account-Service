package account.businesslayer;

import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;
import java.time.YearMonth;
import java.util.Objects;

@Embeddable
public class PaymentId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;

    private YearMonth period;

    public PaymentId() {
    }

    public PaymentId(String username, YearMonth period) {
        this.username = username;
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentId paymentId = (PaymentId) o;
        return Objects.equals(username, paymentId.username) && period.equals(paymentId.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, period);
    }

    public String getUsername() {
        return username;
    }

    public YearMonth getPeriod() {
        return period;
    }
}
