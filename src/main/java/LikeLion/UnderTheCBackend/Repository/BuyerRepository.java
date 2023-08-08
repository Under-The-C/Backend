package LikeLion.UnderTheCBackend.Repository;

import LikeLion.UnderTheCBackend.Entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    Optional<Buyer> findByEmail(String email);
}
