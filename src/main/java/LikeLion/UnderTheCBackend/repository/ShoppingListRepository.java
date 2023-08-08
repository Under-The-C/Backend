package LikeLion.UnderTheCBackend.repository;

import LikeLion.UnderTheCBackend.entity.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Integer> {
    List<ShoppingList> findByBuyerId(Integer buyer_id);
}
