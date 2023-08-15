package account.dto.response;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class PaymentStatusResponse implements Comparable<PaymentStatusResponse>{
    private String name;
    private String lastname;
    private String period;
    private String salary;
    private YearMonth yearMonth;

    public PaymentStatusResponse() {
    }

    public String getName() {
        return name;
    }

    public PaymentStatusResponse addName(String name) {
        this.name = name;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public PaymentStatusResponse addLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getPeriod() {
        return period;
    }

    public PaymentStatusResponse addPeriod(String period) {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-yyyy");
        DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern("LLLL-yyyy");
        this.yearMonth = YearMonth.parse(period, FORMATTER);
        this.period = this.yearMonth.format(FORMATTER2);
        return this;
    }

    public String getSalary() {
        return salary;
    }

    public PaymentStatusResponse addSalary(Long salary) {
        this.salary = salary / 100L + " dollar(s) " + salary % 100 + " cent(s)";
        return this;
    }

    @Override
    public int compareTo(PaymentStatusResponse o) {
        return o.yearMonth.compareTo(this.yearMonth);
    }
}
