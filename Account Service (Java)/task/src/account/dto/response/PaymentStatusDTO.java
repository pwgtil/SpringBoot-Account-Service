package account.dto.response;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class PaymentStatusDTO implements Comparable<PaymentStatusDTO>{
    private String name;
    private String lastname;
    private String period;
    private String salary;
    private YearMonth yearMonth;

    public PaymentStatusDTO() {
    }

    public String getName() {
        return name;
    }

    public PaymentStatusDTO addName(String name) {
        this.name = name;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public PaymentStatusDTO addLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getPeriod() {
        return period;
    }

    public PaymentStatusDTO addPeriod(String period) {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-yyyy");
        DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern("LLLL-yyyy");
        this.yearMonth = YearMonth.parse(period, FORMATTER);
        this.period = this.yearMonth.format(FORMATTER2);
        return this;
    }

    public String getSalary() {
        return salary;
    }

    public PaymentStatusDTO addSalary(Long salary) {
        this.salary = salary / 100L + " dollar(s) " + salary % 100 + " cent(s)";
        return this;
    }

    @Override
    public int compareTo(PaymentStatusDTO o) {
        return o.yearMonth.compareTo(this.yearMonth);
    }
}
