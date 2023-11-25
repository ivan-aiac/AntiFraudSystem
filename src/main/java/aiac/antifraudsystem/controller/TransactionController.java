package aiac.antifraudsystem.controller;

import aiac.antifraudsystem.dto.FeedbackRequest;
import aiac.antifraudsystem.dto.TransactionDTO;
import aiac.antifraudsystem.service.SupportService;
import aiac.antifraudsystem.service.TransactionService;
import aiac.antifraudsystem.validation.CreditCard;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud")
@Validated
public class TransactionController {

    private final TransactionService transactionService;
    private final SupportService supportService;

    @Autowired
    public TransactionController(TransactionService transactionService, SupportService supportService) {
        this.transactionService = transactionService;
        this.supportService = supportService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<?> transferMoney(@RequestBody @Valid TransactionDTO transactionDTO) {
        return transactionService.transferMoney(transactionDTO);
    }

    @PutMapping("/transaction")
    public ResponseEntity<?> addFeedback(@RequestBody @Valid FeedbackRequest feedbackRequest) {
        return supportService.addFeedback(feedbackRequest);
    }

    @GetMapping("/history")
    public List<TransactionDTO> findAllTransactions() {
        return transactionService.findAllTransactions();
    }

    @GetMapping("/history/{number}")
    public ResponseEntity<?> findAllTransactionsByCardNumber(@PathVariable @CreditCard String number) {
        return transactionService.findAllTransactionsByCardNumber(number);
    }
}
