package account.entity;

import jakarta.persistence.*;
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
    private Long salary;


    /*
     * Constructor
     * */
    public Payment() {
    }

    public Payment(String employee, String period, Long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }

    /*
     * Getters
     * */
    public String getEmployee() {
        return employee;
    }

    public String getPeriod() {
        return period;
    }

    public Long getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "username='" + employee + '\'' +
                ", period='" + period + '\'' +
                '}';
    }
}
