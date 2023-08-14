package LikeLion.UnderTheCBackend.repository;

import LikeLion.UnderTheCBackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //**OrderBy 앞에는 And를 붙이지 않음**

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.detailImage")
    List<Product> findAllWithDetailImage();
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.keywords")
    List<Product> findAllWithKeywords();

    Optional<Product> findByName(String productName);
    List<Product> findAllByNameContaining(String partialName);


    List<Product> findAllByNameContainingOrderByPrice(String productName); // 수정: 메서드명 변경

    List<Product> findAllByNameContainingOrderByCreatedAtDesc(String productName); // 수정: 메서드명 변경

    List<Product> findAllByNameContainingAndCategoryOrderByCreatedAtDesc(String productName, String category); // 수정: 메서드명 변경


    List<Product> findAllByNameContainingAndCategoryOrderByPrice(String productName, String category);

    List<Product> findAllByNameContainingAndCategory(String productName, String category);

    List<Product> findAllByNameContainingAndKeywordsContainingOrderByCreatedAtDesc(String productName, String keywords);

    List<Product> findAllByNameContainingAndKeywordsContainingOrderByPrice(String productName, String keywords);

    List<Product> findAllByNameContainingAndKeywordsContaining(String productName, String keywords);

    List<Product> findAllByNameContainingOrderByViewCountDesc(String productName);

    List<Product> findAllByNameContainingAndCategoryOrderByViewCountDesc(String productName, String category);

    List<Product> findAllByNameContainingAndKeywordsContainingOrderByViewCountDesc(String productName, String keywords);

    List<Product> findAllByNameContainingOrProductKeywordsKeywordContainingOrderByCreatedAtDesc(String productName, String productName1);

    List<Product> findAllByNameContainingOrKeywordsKeywordContainingOrderByCreatedAtDesc(String productName, String productName1);

    List<Product> findAllByNameContainingOrKeywordsKeywordContainingOrderByPrice(String search, String search1);

    List<Product> findAllByNameContainingOrKeywordsKeywordContainingOrderByViewCountDesc(String search, String search1);

    List<Product> findAllByNameContainingOrKeywordsKeywordContaining(String search, String search1);

    List<Product> findAllByNameContainingOrKeywordsKeywordContainingOrderByReviewCountDesc(String search, String search1);

    List<Product> findAllByNameContainingOrKeywordsKeywordContainingAndCategoryOrderByPrice(String search, String search1, String category);

    List<Product> findAllByNameContainingOrKeywordsKeywordContainingAndCategoryOrderByViewCountDesc(String search, String search1, String category);

    List<Product> findAllByNameContainingOrKeywordsKeywordContainingAndCategory(String search, String search1, String category);

    List<Product> findAllByNameContainingOrKeywordsKeywordContainingAndCategoryOrderByCreatedAtDesc(String search, String search1, String category);

    List<Product> findAllByNameContainingOrKeywordsKeywordContainingAndCategoryOrderByReviewCountDesc(String search, String search1, String category);
}
