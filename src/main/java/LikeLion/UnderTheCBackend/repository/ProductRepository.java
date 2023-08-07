package LikeLion.UnderTheCBackend.repository;

import LikeLion.UnderTheCBackend.Entity.Product;
import org.springdoc.core.converters.models.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.Date;

public interface ProductRepository extends JpaRepository<Product, String> {
}
