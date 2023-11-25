package aiac.antifraudsystem.service;

import aiac.antifraudsystem.dto.FeedbackRequest;
import aiac.antifraudsystem.dto.TransactionDTO;
import aiac.antifraudsystem.enums.LimitOperation;
import aiac.antifraudsystem.enums.TransactionType;
import aiac.antifraudsystem.model.AmountLimit;
import aiac.antifraudsystem.model.Transaction;
import aiac.antifraudsystem.repository.LimitsRepository;
import aiac.antifraudsystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.LongConsumer;
import java.util.function.LongSupplier;

import static aiac.antifraudsystem.enums.LimitOperation.*;
import static aiac.antifraudsystem.enums.TransactionType.*;

@Service
public class SupportService {

    private final TransactionRepository transactionRepository;
    private final LimitsRepository limitsRepository;
    @Autowired
    public SupportService(TransactionRepository transactionRepository,
                          LimitsRepository limitsRepository) {
        this.transactionRepository = transactionRepository;
        this.limitsRepository = limitsRepository;
    }

    @Transactional
    public ResponseEntity<?> addFeedback(FeedbackRequest feedbackRequest) {
        Transaction transaction = transactionRepository.findById(feedbackRequest.getTransactionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
        if (!transaction.getFeedback().getMessage().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Feedback already set");
        }
        if (transaction.getType() == feedbackRequest.getFeedback()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Feedback cannot be equal");
        }
        updateCardAmountLimit(transaction.getNumber(), transaction.getAmount(), transaction.getType(), feedbackRequest.getFeedback());
        transaction.getFeedback().setMessage(feedbackRequest.getFeedback().name());
        TransactionDTO dto = TransactionDTO.of(transactionRepository.save(transaction));
        return ResponseEntity.ok(dto);
    }

    private void updateCardAmountLimit(String cardNumber, long transactionAmount, TransactionType validation, TransactionType feedback) {
        AmountLimit limit = limitsRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        if (validation == ALLOWED) {
            if (feedback == PROHIBITED) {
                updateLimitAmount(limit::getManual, transactionAmount, limit::setManual, DECREASE);
            }
            //MANUAL & PROHIBITED
            updateLimitAmount(limit::getAllowed, transactionAmount, limit::setAllowed, DECREASE);
        } else if (validation == MANUAL_PROCESSING) {
            if (feedback == ALLOWED) {
                updateLimitAmount(limit::getAllowed, transactionAmount, limit::setAllowed, INCREASE);
            } else { //PROHIBITED
                updateLimitAmount(limit::getManual, transactionAmount, limit::setManual, DECREASE);
            }
        } else { //validation = PROHIBITED
            if (feedback == ALLOWED) {
                updateLimitAmount(limit::getAllowed, transactionAmount, limit::setAllowed, INCREASE);
            }
            //ALLOWED & MANUAL
            updateLimitAmount(limit::getManual, transactionAmount, limit::setManual, INCREASE);
        }
        limitsRepository.save(limit);
    }

    private void updateLimitAmount(LongSupplier limitAmount, long transactionAmount, LongConsumer setter, LimitOperation operation) {
        long newLimit = operation.applyFunction(limitAmount.getAsLong(), transactionAmount);
        setter.accept(newLimit);
    }

}
