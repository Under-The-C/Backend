package LikeLion.UnderTheCBackend.repository;

import LikeLion.UnderTheCBackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
