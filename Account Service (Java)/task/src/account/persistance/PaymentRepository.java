package account.persistance;

import account.businesslayer.Payment;
import account.businesslayer.PaymentId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, PaymentId> {
    Payment findPaymentByEmployeeIgnoreCaseAndPeriod(String email, String period);

    List<Payment> findByEmployee(String email);
}
