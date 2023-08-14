package account.controller;

import account.entity.Payment;
import account.service.PaymentService;
import account.dto.response.StatusDTO;
import account.controller.routing.Payments;
import jakarta.validation.Valid;
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
    @ResponseStatus(HttpStatus.OK)
    public StatusDTO createPayments(@Valid @RequestBody List<Payment> paymentList) {
        paymentService.createMultiple(paymentList);
        return new StatusDTO("Added successfully!");
    }

    @PutMapping(Payments.PATH)
    @ResponseStatus(HttpStatus.OK)
    public StatusDTO updatePayments(@Valid @RequestBody Payment payment) {
        paymentService.update(payment);
        return new StatusDTO("Updated successfully!");
    }

    @GetMapping(account.controller.routing.Payment.PATH)
    public ResponseEntity<?> getPayment(@RequestParam(required = false) String period, @AuthenticationPrincipal UserDetails user) {
        if (user == null) {
            return new ResponseEntity<>(new StatusDTO("User must login!"), HttpStatus.OK);
        }

        if (period != null) {
            return new ResponseEntity<>(paymentService.getPaymentDetails(period, user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(paymentService.getAllPaymentDetails(user), HttpStatus.OK);
        }
    }

}

