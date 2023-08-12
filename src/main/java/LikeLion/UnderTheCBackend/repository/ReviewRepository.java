package LikeLion.UnderTheCBackend.repository;

import LikeLion.UnderTheCBackend.entity.Product;
import LikeLion.UnderTheCBackend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>  {
    @Query("SELECT DISTINCT r FROM Review r LEFT JOIN FETCH r.reviewImage")
    List<Review> findAllWithReviewImage();
}
