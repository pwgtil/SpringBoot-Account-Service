package account.service;

import account.entity.Payment;
import account.repository.PaymentRepository;
import account.dto.response.PaymentStatusDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    private final UserServiceGetInfo userServiceGetInfo;

    public PaymentService(@Autowired PaymentRepository paymentRepository, @Autowired UserServiceGetInfo userServiceGetInfo) {
        this.paymentRepository = paymentRepository;
        this.userServiceGetInfo = userServiceGetInfo;
    }

    public Optional<Payment> getPayment(String email, String period) {
        return Optional.ofNullable(paymentRepository.findPaymentByEmployeeIgnoreCaseAndPeriod(email, period));
    }

    public Payment create(@NotNull Payment payment) {
        paymentValidations(payment);
        if (getPayment(payment.getEmployee(), payment.getPeriod()).isEmpty()) {
            return paymentRepository.save(payment);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, payment + " already exits!");
        }
    }

    public Payment update(@NotNull Payment payment) {
        paymentValidations(payment);
        if (getPayment(payment.getEmployee(), payment.getPeriod()).isPresent()) {
            return paymentRepository.save(payment);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, payment + " does not exist!");
        }
    }

    @Transactional
    public void createMultiple(List<Payment> payments) {
        for (Payment payment : payments) {
            create(payment);
        }
    }

    public PaymentStatusDTO getPaymentDetails(String period, UserDetails userDetail) {
        Payment payment = getPayment(userDetail.getUsername(), period).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, period + " not found in the DB for user: " + userDetail.getUsername())
        );
        UserGetInfo user = userServiceGetInfo.getUserInfo(userDetail.getUsername());
        return new PaymentStatusDTO()
                .addName(user.getName())
                .addLastname(user.getLastname())
                .addPeriod(period)
                .addSalary(payment.getSalary());
    }

    public List<PaymentStatusDTO> getAllPaymentDetails(UserDetails userDetail) {
        List<Payment> paymentList = paymentRepository.findByEmployee(userDetail.getUsername());
        UserGetInfo user = userServiceGetInfo.getUserInfo(userDetail.getUsername());
        return paymentList.stream().map(payment ->
                        new PaymentStatusDTO()
                                .addName(user.getName())
                                .addLastname(user.getLastname())
                                .addPeriod(payment.getPeriod())
                                .addSalary(payment.getSalary())
                ).sorted()
                .collect(Collectors.toList());
    }

    private void paymentValidations(Payment payment) {
        assertValidUserAccount(payment.getEmployee());
        assertValidPeriod(payment.getPeriod());
        assertValidSalary(payment.getSalary());
    }

    private void assertValidUserAccount(String username) {
        if (userServiceGetInfo.getUserInfo(username) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account " + username + " does not exist");
        }
    }

    private void assertValidPeriod(String period) {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-yyyy");
        try {
            YearMonth.parse(period, FORMATTER);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, period + " is invalid");
        }
    }

    private void assertValidSalary(Long salary) {
        if (salary < 0L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The salary must not be negative");
        }
    }
}
