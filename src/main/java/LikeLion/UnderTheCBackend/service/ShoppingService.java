package LikeLion.UnderTheCBackend.service;

import LikeLion.UnderTheCBackend.entity.Product;
import LikeLion.UnderTheCBackend.entity.ShoppingHistory;
import LikeLion.UnderTheCBackend.entity.ShoppingList;
import LikeLion.UnderTheCBackend.entity.User;
import LikeLion.UnderTheCBackend.repository.ShoppingHistoryRepository;
import LikeLion.UnderTheCBackend.repository.ShoppingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class ShoppingService {
    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingHistoryRepository shoppingHistoryRepository;

    @Autowired
    ShoppingService(ShoppingListRepository shoppingListRepository, ShoppingHistoryRepository shoppingHistoryRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingHistoryRepository = shoppingHistoryRepository;
    }

    public List<ShoppingList> findListByUserId(Long userId) {
        return shoppingListRepository.findByUserId_Id(userId);
    }

    @Transactional
    public List<ShoppingList> addShoppingList(User user, Product product, Integer count) {

        /* 입력된 상품의 개수만큼 장바구니에 상품 추가 */
        for (int i=0; i<count; ++i ){
            ShoppingList shoppingList = new ShoppingList(user, product, count);
            shoppingListRepository.save(shoppingList);
        }

        return shoppingListRepository.findByUserId_Id(user.getId());
    }

    @Transactional
    public ShoppingList deleteByProductId(Long userId, Long productId, Integer count) {
        Optional<ShoppingList> optShoppingList = shoppingListRepository.findByUserId_IdAndProductId(userId, productId);
        ShoppingList shoppingList = optShoppingList.orElseThrow(() ->
                new ResponseStatusException(BAD_REQUEST, "해당 상품이 존재하지 않습니다."));
        if (shoppingList.getCount() > count) {
            shoppingList.setCount(shoppingList.getCount() - count);
            shoppingListRepository.save(shoppingList);
        } else {
            shoppingListRepository.delete(shoppingList);
            shoppingList.setCount(0);
        }
        return shoppingList;
    }

    @Transactional
    public List<ShoppingHistory> findHistoryByUserId(Long id) {
        return shoppingHistoryRepository.findByUserId_Id(id);
    }
}
