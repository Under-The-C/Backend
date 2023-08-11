package LikeLion.UnderTheCBackend.service;

import LikeLion.UnderTheCBackend.repository.ShoppingHistoryRepository;
import LikeLion.UnderTheCBackend.repository.ShoppingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingService {
    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingHistoryRepository shoppingHistoryRepository;

    @Autowired
    ShoppingService(ShoppingListRepository shoppingListRepository, ShoppingHistoryRepository shoppingHistoryRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingHistoryRepository = shoppingHistoryRepository;
    }

}
