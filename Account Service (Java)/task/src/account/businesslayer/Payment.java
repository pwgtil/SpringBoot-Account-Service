package account.businesslayer;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "payments")
@IdClass(PaymentId.class)
public class Payment {
    /*
    * Properties
    * */
    @Id
    @NotEmpty
    private String employee;

    @Id
//    @Convert(converter = YearMonthConverter.class)
    private String period;

    @Column
    @Min(value = 0L, message = "The salary must not be negative")
    private Long salary;

    public Payment() {
    }

    public Payment(String employee, String period, Long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String username) {
        this.employee = username;
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
                "username='" + employee + '\'' +
                ", period='" + period + '\'' +
                '}';
    }
}
