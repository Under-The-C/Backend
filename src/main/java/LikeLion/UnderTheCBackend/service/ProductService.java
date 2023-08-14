package LikeLion.UnderTheCBackend.service;

import LikeLion.UnderTheCBackend.entity.Product;
import LikeLion.UnderTheCBackend.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "product_id가 올바르지 않습니다.")
        );
    }
}
