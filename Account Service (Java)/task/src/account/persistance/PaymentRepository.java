package account.persistance;

import account.businesslayer.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {
    Payment findPaymentByUsernameIgnoreCaseAndPeriod(String email, String period);
}
