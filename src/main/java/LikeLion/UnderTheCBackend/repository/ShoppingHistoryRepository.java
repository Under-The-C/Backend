package LikeLion.UnderTheCBackend.repository;

import LikeLion.UnderTheCBackend.entity.ShoppingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingHistoryRepository extends JpaRepository<ShoppingHistory, Long> {
    List<ShoppingHistory> findByUserId_Id(Long userId);

    List<ShoppingHistory> findByUserId_IdAndImpUid(Long userId, String impUid);
}
