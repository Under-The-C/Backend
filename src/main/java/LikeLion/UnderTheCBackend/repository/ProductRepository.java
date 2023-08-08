package LikeLion.UnderTheCBackend.repository;

import LikeLion.UnderTheCBackend.entity.Product;
import org.springdoc.core.converters.models.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.Date;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByName(String productName);

/*
    List<Product> findByNameAndOrderByPrice(String sortBy, String by);

    List<Product> findByNameAndOrderByCreated_atDesc(String productName, String sortBy);

    List<Product> findAllByNameAndCategoryAndOrderByCreated_atDesc(String productName, String category, String sortBy);

    List<Product> findAllByNameAndOrderByCreated_atDesc(String productName, String sortBy);*/
}
