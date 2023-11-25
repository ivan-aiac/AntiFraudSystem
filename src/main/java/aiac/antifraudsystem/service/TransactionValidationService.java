package aiac.antifraudsystem.service;

import aiac.antifraudsystem.dto.TransactionDTO;
import aiac.antifraudsystem.enums.Region;
import aiac.antifraudsystem.enums.Rejections;
import aiac.antifraudsystem.enums.TransactionType;
import aiac.antifraudsystem.model.*;
import aiac.antifraudsystem.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static aiac.antifraudsystem.enums.Rejections.*;
import static aiac.antifraudsystem.enums.TransactionType.*;

@Service
public class TransactionValidationService {
    private final AntiFraudService antiFraudService;
    private final TransactionRepository repository;

    public TransactionValidationService(AntiFraudService antiFraudService,
                                        TransactionRepository repository) {
        this.antiFraudService = antiFraudService;
        this.repository = repository;
    }

    public TransactionValidation validateTransaction(TransactionDTO transactionDTO, AmountLimit amountLimit) {
        boolean isCardStolen = antiFraudService.isCardStolen(transactionDTO.getNumber());
        boolean isIpSuspicious = antiFraudService.isIpSuspicious(transactionDTO.getIp());
        long regionCount = findRegionCount(transactionDTO.getNumber(), transactionDTO.getDate(), transactionDTO.getRegion());
        long ipCount = findIpCount(transactionDTO.getNumber(), transactionDTO.getIp(), transactionDTO.getDate());

        TransactionType amountType = findTransactionTypeByAmount(transactionDTO.getAmount(), amountLimit);
        TransactionType correlationType = validateTypeByCorrelation(amountType, regionCount, ipCount);
        TransactionType result = validateTypeIfFraudulent(correlationType, isCardStolen, isIpSuspicious);

        String rejections = findTransactionRejections(amountType, isCardStolen,
                isIpSuspicious, ipCount, regionCount);

        return new TransactionValidation(result, rejections);
    }

    private TransactionType findTransactionTypeByAmount(long amount, AmountLimit limit) {
        if (amount <= limit.getAllowed()) {
            return ALLOWED;
        } else if (amount <= limit.getManual()) {
            return MANUAL_PROCESSING;
        } else {
            return PROHIBITED;
        }
    }

    private TransactionType validateTypeByCorrelation(TransactionType type, long regionCount, long ipCount) {
        if (regionCount < 2 && ipCount < 2) {
            return type;
        } else if (regionCount == 2 || ipCount == 2) {
            return type == PROHIBITED ? PROHIBITED : MANUAL_PROCESSING;
        } else {
            return PROHIBITED;
        }
    }

    private TransactionType validateTypeIfFraudulent(TransactionType type, boolean isCardStolen, boolean isIpSuspicious) {
        if (isCardStolen || isIpSuspicious) {
            return PROHIBITED;
        }
        return type;
    }

    private long findRegionCount(String cardNumber, LocalDateTime date, Region region) {
        return repository.countBetweenDatesByNumberExcRegion(cardNumber,
                date, date.minusHours(1), region);
    }

    private long findIpCount(String cardNumber, String requestIp, LocalDateTime date) {
        Set<String> ipSet = new HashSet<>();
        Pageable pageRequest = PageRequest.of(0, 20);
        do {
            Page<Transaction> page =  repository.findAllBetweenDatesByNumber(
                    cardNumber, date, date.minusHours(1), pageRequest);
            List<String> ipList = page.stream()
                    .map(Transaction::getIp)
                    .filter(ip -> !ip.equals(requestIp))
                    .toList();
            ipSet.addAll(ipList);
            pageRequest = page.nextPageable();
        } while (pageRequest.isPaged() && ipSet.size() < 3);
        return ipSet.size();
    }

    private String findTransactionRejections(TransactionType amountType, boolean isStolen,
                                             boolean isSuspicious, long ipCount, long regionCount) {
        List<Rejections> rejections = new ArrayList<>();
        if (isStolen)
            rejections.add(STOLEN_CARD);
        if (isSuspicious)
            rejections.add(SUS_IP);
        if ((!isStolen && !isSuspicious && amountType != ALLOWED)
                || ((isStolen || isSuspicious) && amountType == PROHIBITED))
            rejections.add(HIGH_AMOUNT);
        if (ipCount >= 2)
            rejections.add(MULTIPLE_IP);
        if (regionCount >= 2)
            rejections.add(MULTIPLE_REGION);
        if (rejections.isEmpty()) {
            rejections.add(NONE);
        }
        return rejections.stream()
                .map(Rejections::getReason)
                .sorted()
                .collect(Collectors.joining(", "));
    }

}
