package account.businesslayer;

import account.businesslayer.converter.YearMonthConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.YearMonth;

@Entity
@Table(name = "payments")
@IdClass(PaymentId.class)
public class Payment {
    /*
    * Properties
    * */
    @Id
    @NotEmpty
    private String username;

    @Id
    @Convert(converter = YearMonthConverter.class)
    private String period;

    @Column
    @Min(value = 0L, message = "The salary must not be negative")
    private Long salary;

    public Payment() {
    }

    public Payment(String username, String period, Long salary) {
        this.username = username;
        this.period = period;
        this.salary = salary;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "username='" + username + '\'' +
                ", period='" + period + '\'' +
                '}';
    }
}
