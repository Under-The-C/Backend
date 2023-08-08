package LikeLion.UnderTheCBackend.repository;

import LikeLion.UnderTheCBackend.entity.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Integer> {
}
