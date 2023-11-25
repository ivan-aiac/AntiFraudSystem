package aiac.antifraudsystem.service;

import aiac.antifraudsystem.dto.*;
import aiac.antifraudsystem.model.StolenCard;
import aiac.antifraudsystem.model.SuspiciousIp;
import aiac.antifraudsystem.repository.StolenCardRepository;
import aiac.antifraudsystem.repository.SuspiciousIpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AntiFraudService {

    private final SuspiciousIpRepository suspiciousIpRepository;
    private final StolenCardRepository stolenCardRepository;

    @Autowired
    public AntiFraudService(SuspiciousIpRepository suspiciousIpRepository,
                            StolenCardRepository stolenCardRepository) {
        this.suspiciousIpRepository = suspiciousIpRepository;
        this.stolenCardRepository = stolenCardRepository;
    }

    public ResponseEntity<?> saveSuspiciousIp(SusIpDTO susIpDTO) {
        if (suspiciousIpRepository.existsByIp(susIpDTO.getIp())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "IP already registered");
        }
        SuspiciousIp suspiciousIp = suspiciousIpRepository.save(susIpDTO.toSuspiciousIp());
        return ResponseEntity.ok(SusIpDTO.of(suspiciousIp));
    }

    public ResponseEntity<?> deleteSuspiciousIp(String ip) {
        SuspiciousIp suspiciousIp = findIpOrThrowNotFound(ip);
        suspiciousIpRepository.delete(suspiciousIp);
        SimpleResponse response = new SimpleResponse("IP %s successfully removed!".formatted(ip));
        return ResponseEntity.ok(response);
    }

    public List<SusIpDTO> findAllSuspiciousIp() {
        return suspiciousIpRepository.findAllByOrderById().stream()
                .map(SusIpDTO::of)
                .toList();
    }

    private SuspiciousIp findIpOrThrowNotFound(String ip) {
        return suspiciousIpRepository.findByIp(ip)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "IP not found"));
    }

    public ResponseEntity<?> addStolenCard(StolenCardDTO stolenCardDTO) {
        if (stolenCardRepository.existsByNumber(stolenCardDTO.getNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Card already registered");
        }
        StolenCard stolenCard = stolenCardRepository.save(stolenCardDTO.toStolenCard());
        return ResponseEntity.ok(StolenCardDTO.of(stolenCard));
    }

    public ResponseEntity<?> deleteStolenCard(String number) {
        StolenCard stolenCard = findStolenCardOrThrowNotFound(number);
        stolenCardRepository.delete(stolenCard);
        SimpleResponse response = new SimpleResponse("Card %s successfully removed!".formatted(number));
        return ResponseEntity.ok(response);
    }

    public List<StolenCardDTO> findAllStolenCards() {
        return stolenCardRepository.findAllByOrderById().stream()
                .map(StolenCardDTO::of)
                .toList();
    }

    private StolenCard findStolenCardOrThrowNotFound(String number) {
        return stolenCardRepository.findByNumber(number)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found"));
    }

    public boolean isCardStolen(String cardNumber) {
        return stolenCardRepository.existsByNumber(cardNumber);
    }

    public boolean isIpSuspicious(String ip) {
        return suspiciousIpRepository.existsByIp(ip);
    }

}
