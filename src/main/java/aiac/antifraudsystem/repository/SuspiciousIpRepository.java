package aiac.antifraudsystem.repository;

import aiac.antifraudsystem.model.SuspiciousIp;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface SuspiciousIpRepository extends ListCrudRepository<SuspiciousIp, Long> {
    Optional<SuspiciousIp> findByIp(String ip);
    boolean existsByIp(String ip);
    List<SuspiciousIp> findAllByOrderById();
}
