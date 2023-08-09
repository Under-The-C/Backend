package LikeLion.UnderTheCBackend.repository;

import LikeLion.UnderTheCBackend.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long>{
}
