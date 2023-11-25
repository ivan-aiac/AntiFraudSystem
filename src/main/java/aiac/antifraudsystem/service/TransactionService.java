package aiac.antifraudsystem.service;

import aiac.antifraudsystem.dto.TransactionDTO;
import aiac.antifraudsystem.dto.TransactionResponse;
import aiac.antifraudsystem.model.*;
import aiac.antifraudsystem.repository.LimitsRepository;
import aiac.antifraudsystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionValidationService validationService;
    private final LimitsRepository limitsRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              TransactionValidationService validationService,
                              LimitsRepository limitsRepository) {
        this.transactionRepository = transactionRepository;
        this.validationService = validationService;
        this.limitsRepository = limitsRepository;
    }

    @Transactional
    public ResponseEntity<?> transferMoney(TransactionDTO transactionDTO) {
        AmountLimit amountLimit = limitsRepository.findByCardNumber(transactionDTO.getNumber())
                .orElseGet(() -> limitsRepository.save(new AmountLimit(transactionDTO.getNumber())));
        TransactionValidation validation = validationService.validateTransaction(transactionDTO, amountLimit);

        Transaction transaction = transactionDTO.toTransaction();
        transaction.setType(validation.result());

        transactionRepository.save(transaction);

        TransactionResponse response = new TransactionResponse(validation.result(), validation.rejections());
        return ResponseEntity.ok(response);
    }

    public List<TransactionDTO> findAllTransactions() {
        var transactions = transactionRepository.findAll(Sort.by("id"));
        return StreamSupport.stream(transactions.spliterator(), false)
                .map(TransactionDTO::of)
                .toList();
    }

    public ResponseEntity<?> findAllTransactionsByCardNumber(String cardNumber) {
        List<Transaction> transactions = transactionRepository.findAllByNumber(cardNumber);
        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transactions not found");
        }
        return ResponseEntity.ok(transactions.stream()
                .map(TransactionDTO::of)
                .toList());
    }
}
