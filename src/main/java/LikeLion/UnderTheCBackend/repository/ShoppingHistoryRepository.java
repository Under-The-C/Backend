package LikeLion.UnderTheCBackend.repository;

import LikeLion.UnderTheCBackend.entity.ShoppingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingHistoryRepository extends JpaRepository<ShoppingHistory, Integer> {
}
