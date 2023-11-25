package aiac.antifraudsystem.controller;

import aiac.antifraudsystem.dto.StolenCardDTO;
import aiac.antifraudsystem.dto.SusIpDTO;
import aiac.antifraudsystem.service.AntiFraudService;
import aiac.antifraudsystem.validation.CreditCard;
import aiac.antifraudsystem.validation.IPv4;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud")
@Validated
public class AntiFraudController {

    private final AntiFraudService antiFraudService;

    @Autowired
    public AntiFraudController(AntiFraudService antiFraudService) {
        this.antiFraudService = antiFraudService;
    }

    @PostMapping("/suspicious-ip")
    public ResponseEntity<?> registerSuspiciousIp(@RequestBody @Valid SusIpDTO susIp) {
        return antiFraudService.saveSuspiciousIp(susIp);
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<?> deleteSuspiciousIp(@PathVariable @IPv4 String ip) {
        return antiFraudService.deleteSuspiciousIp(ip);
    }

    @GetMapping("/suspicious-ip")
    public List<SusIpDTO> findAllSuspiciousIp() {
        return antiFraudService.findAllSuspiciousIp();
    }

    @PostMapping("/stolencard")
    public ResponseEntity<?> addStolenCard(@RequestBody @Valid StolenCardDTO stolenCardDTO) {
        return antiFraudService.addStolenCard(stolenCardDTO);
    }

    @DeleteMapping("/stolencard/{number}")
    public ResponseEntity<?> deleteStolenCard(@PathVariable @CreditCard String number) {
        return antiFraudService.deleteStolenCard(number);
    }

    @GetMapping("/stolencard")
    public List<StolenCardDTO> findAllStolenCards() {
        return antiFraudService.findAllStolenCards();
    }

}
