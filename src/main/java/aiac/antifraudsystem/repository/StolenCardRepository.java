package aiac.antifraudsystem.repository;

import aiac.antifraudsystem.model.StolenCard;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface StolenCardRepository extends ListCrudRepository<StolenCard, Long> {
    Optional<StolenCard> findByNumber(String number);
    boolean existsByNumber(String number);
    List<StolenCard> findAllByOrderById();
}
