package aiac.antifraudsystem.repository;

import aiac.antifraudsystem.enums.Region;
import aiac.antifraudsystem.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long>, ListCrudRepository<Transaction, Long> {

    @Query("""
        SELECT COUNT(DISTINCT t.region) FROM Transaction t\s
        WHERE t.number = :cardNumber\s
        AND NOT t.region = :region\s
        AND t.date BETWEEN :toDate AND :fromDate
        """)
    Long countBetweenDatesByNumberExcRegion(@Param("cardNumber") String cardNumber,
                                            @Param("fromDate") LocalDateTime fromDate,
                                            @Param("toDate") LocalDateTime toDate,
                                            @Param("region") Region region);

    @Query("""
        SELECT t FROM Transaction t\s
        WHERE t.number = :cardNumber\s
        AND t.date BETWEEN :toDate AND :fromDate
        """)
    Page<Transaction> findAllBetweenDatesByNumber(@Param("cardNumber") String cardNumber,
                                                  @Param("fromDate") LocalDateTime fromDate,
                                                  @Param("toDate") LocalDateTime toDate,
                                                  Pageable pageable);

    List<Transaction> findAllByNumber(String number);

    Optional<Transaction> findById(long id);

}
