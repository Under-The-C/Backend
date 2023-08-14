package LikeLion.UnderTheCBackend.repository;

import LikeLion.UnderTheCBackend.entity.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    List<ShoppingList> findByUserId_Id(Long user_id);

    Optional<ShoppingList> findByUserId_IdAndProductId(Long user_id, Long product_id);
}
