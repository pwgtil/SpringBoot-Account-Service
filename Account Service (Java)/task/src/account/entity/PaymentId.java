package account.entity;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;

public class PaymentId implements Serializable {


    private String employee;

    @Column(name = "period")
    private String period;

    public PaymentId() {
    }

    public PaymentId(String employee, String period) {
        this.employee = employee;
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentId paymentId = (PaymentId) o;
        return Objects.equals(employee, paymentId.employee) && period.equals(paymentId.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, period);
    }

}
