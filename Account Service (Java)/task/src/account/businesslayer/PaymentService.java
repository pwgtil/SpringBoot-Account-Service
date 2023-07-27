package account.businesslayer;

import account.persistance.PaymentRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(@Autowired PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Optional<Payment> getPayment(String email, String period) {
        return Optional.ofNullable(paymentRepository.findPaymentByUsernameIgnoreCaseAndPeriod(email, period));
    }

    public Payment create(@NotNull Payment payment) {
        if (getPayment(payment.getUsername(), payment.getPeriod()).isEmpty()) {
            return paymentRepository.save(payment);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, payment.toString() + " already exits!");
        }
    }

    public Payment update(@NotNull Payment payment) {
        return paymentRepository
                .save(getPayment(payment.getUsername(), payment.getPeriod())
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.BAD_REQUEST, payment.toString() + " does not exist!")
                        ));
    }

    @Transactional
    public void createMultiple(List<Payment> payments) {
        for (Payment payment : payments) {
            create(payment);
        }
    }
}
