package account.controller;

import account.entity.Payment;
import account.service.PaymentService;
import account.dto.response.StatusDTO;
import account.controller.routing.Payments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class PaymentController {

    final PaymentService paymentService;

    public PaymentController(@Autowired PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(Payments.PATH)
    public ResponseEntity<StatusDTO> createPayments(@RequestBody List<Payment> paymentList) {
        paymentService.createMultiple(paymentList);
        return new ResponseEntity<>(new StatusDTO("Added successfully!"), HttpStatus.OK);
    }

    @PutMapping(Payments.PATH)
    public ResponseEntity<StatusDTO> updatePayments(@RequestBody Payment payment) {
        paymentService.update(payment);
        return new ResponseEntity<>(new StatusDTO("Updated successfully!"), HttpStatus.OK);
    }

    @GetMapping(account.controller.routing.Payment.PATH)
    public ResponseEntity<?> getPayment(@RequestParam(required = false) String period, @AuthenticationPrincipal UserDetails user) {
        if (period != null) {
            return new ResponseEntity<>(paymentService.getPaymentDetails(period, user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(paymentService.getAllPaymentDetails(user), HttpStatus.OK);
        }
    }

}

