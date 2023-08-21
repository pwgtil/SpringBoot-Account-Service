package account.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public String toString() {
        return "Payment{" +
                "username='" + employee + '\'' +
                ", period='" + period + '\'' +
                '}';
    }
}
