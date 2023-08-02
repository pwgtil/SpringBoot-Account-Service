package account.businesslayer;

import account.entity.Payment;
import account.repository.PaymentRepository;
import account.dto.response.PaymentStatusDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private final UserDetailsService userDetailsService;

    public PaymentService(@Autowired PaymentRepository paymentRepository, @Autowired UserDetailsService service) {
        this.paymentRepository = paymentRepository;
        this.userDetailsService = service;
    }

    public Optional<Payment> getPayment(String email, String period) {
        return Optional.ofNullable(paymentRepository.findPaymentByEmployeeIgnoreCaseAndPeriod(email, period));
    }

    public Payment create(@NotNull Payment payment) {
        assertValidUserAccount(payment.getEmployee());
        assertValidPeriod(payment.getPeriod());
        if (getPayment(payment.getEmployee(), payment.getPeriod()).isEmpty()) {
            return paymentRepository.save(payment);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, payment.toString() + " already exits!");
        }
    }

    public Payment update(@NotNull Payment payment) {
        assertValidUserAccount(payment.getEmployee());
        assertValidPeriod(payment.getPeriod());
        if (getPayment(payment.getEmployee(), payment.getPeriod()).isPresent()) {
            return paymentRepository.save(payment);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, payment.toString() + " does not exist!");
        }
    }

    @Transactional
    public void createMultiple(List<Payment> payments) {
        for (Payment payment : payments) {
            create(payment);
        }
    }

    public PaymentStatusDTO getPaymentDetails(String period, UserDetails user) {
        Payment payment = getPayment(user.getUsername(), period).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, period + " not found in the DB for user: " + user.getUsername())
        );
        UserGetInfo userInfo = (UserGetInfo) user;
        return new PaymentStatusDTO()
                .addName(userInfo.getName())
                .addLastname(userInfo.getLastname())
                .addPeriod(period)
                .addSalary(payment.getSalary());
    }

    public List<PaymentStatusDTO> getAllPaymentDetails(UserDetails user) {
        List<Payment> paymentList = paymentRepository.findByEmployee(user.getUsername());
        UserGetInfo userInfo = (UserGetInfo) user;
        return paymentList.stream().map(payment ->
                        new PaymentStatusDTO()
                                .addName(userInfo.getName())
                                .addLastname(userInfo.getLastname())
                                .addPeriod(payment.getPeriod())
                                .addSalary(payment.getSalary())
                ).sorted()
                .collect(Collectors.toList());
    }

    private void assertValidUserAccount(String username) {
        if (userDetailsService.loadUserByUsername(username) == null) {
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
}
