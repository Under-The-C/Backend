package LikeLion.UnderTheCBackend.repository;

import LikeLion.UnderTheCBackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    //**OrderBy 앞에는 And를 붙이지 않음**

    Optional<Product> findByName(String productName);
    List<Product> findAllByNameContaining(String partialName);


    List<Product> findAllByNameContainingOrderByPrice(String productName); // 수정: 메서드명 변경

    List<Product> findAllByNameContainingOrderByCreatedAtDesc(String productName); // 수정: 메서드명 변경

    List<Product> findAllByNameContainingAndCategoryOrderByCreatedAtDesc(String productName, String category); // 수정: 메서드명 변경


    List<Product> findAllByNameContainingAndCategoryOrderByPrice(String productName, String category);

    List<Product> findAllByNameContainingAndCategory(String productName, String category);

    List<Product> findAllByNameContainingAndKeywordContainingOrderByCreatedAtDesc(String productName, String keyword);

    List<Product> findAllByNameContainingAndKeywordContainingOrderByPrice(String productName, String keyword);

    List<Product> findAllByNameContainingAndKeywordContaining(String productName, String keyword);
}
