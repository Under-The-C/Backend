package LikeLion.UnderTheCBackend.repository;

import LikeLion.UnderTheCBackend.entity.ShoppingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingHistoryRepository extends JpaRepository<ShoppingHistory, Long> {
    List<ShoppingHistory> findByBuyerId_Id(Long buyerId);

    List<ShoppingHistory> findByBuyerId_IdAndImpUid(Long buyerId, String impUid);
}
