package aiac.antifraudsystem.repository;

import aiac.antifraudsystem.model.AmountLimit;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface LimitsRepository extends ListCrudRepository<AmountLimit, Long> {
    Optional<AmountLimit> findByCardNumber(String cardNumber);
}
