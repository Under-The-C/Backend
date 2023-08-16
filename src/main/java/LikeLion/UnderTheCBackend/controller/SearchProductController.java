package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.entity.Product;
import LikeLion.UnderTheCBackend.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "SearchProduct API", description = "상품 검색 API")
@RequestMapping("/api/v1/search_product")

public class SearchProductController {
    ProductRepository productRepository;

    SearchProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/all")
    @Operation(summary = "모든 판매 상품 검색 (최신순 or 조회순 or 리뷰순 or 가격순)", description = "Product 테이블에서 Name으로 검색하여 " +
            "정렬된 Product 객체 반환", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public List<Product> searchAll(
            @RequestParam("Name") String search,
            //@RequestParam("Category") String category,
            @RequestParam("sortBy") String sortBy
    ) {
        // **조회순, 리뷰순 정렬을 위해 조회, 리뷰 카운트 값 테이블에 추가 필요함**
        List<Product> product;
        if (sortBy.equals("최신순")) {//엔티티 참조에 매우 유의할것...
            product = productRepository.findAllByNameContainingOrKeywordsKeywordContainingOrderByCreatedAtDesc(search, search);
        } else if (sortBy.equals("가격순")) {
            product = productRepository.findAllByNameContainingOrKeywordsKeywordContainingOrderByPrice(search, search);
        } else if (sortBy.equals("조회순")) {
            product = productRepository.findAllByNameContainingOrKeywordsKeywordContainingOrderByViewCountDesc(search, search);
        } else if (sortBy.equals("리뷰순")) {
            product = productRepository.findAllByNameContainingOrKeywordsKeywordContainingOrderByReviewCountDesc(search, search);
        }else {
            product = productRepository.findAllByNameContainingOrKeywordsKeywordContaining(search, search);
        }
        return product;
    }

    @GetMapping("/category")
    @Operation(summary = "판매 상품 카테고리 검색 (최신순 or 조회순 or 리뷰순 or 가격순)", description = "Product 테이블에서 Name으로 검색하여 " +
            "선택한 Category의 정렬된 Product 객체 반환", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public List<Product> searchByCategory(
            @RequestParam("Name") String search,
            @RequestParam("Category") String category,
            @RequestParam("sortBy") String sortBy
    ) {
        List<Product> product;
        if (sortBy.equals("최신순")) {
                product = productRepository.findAllByNameContainingOrKeywordsKeywordContainingOrderByCreatedAtDesc(search,search);
            }else if (sortBy.equals("가격순")) {
                product = productRepository.findAllByNameContainingOrKeywordsKeywordContainingOrderByPrice(search,search);
            }else if (sortBy.equals("조회순")) {
                product = productRepository.findAllByNameContainingOrKeywordsKeywordContainingOrderByViewCountDesc(search,search);
            }else if (sortBy.equals("리뷰순")) {
                product = productRepository.findAllByNameContainingOrKeywordsKeywordContainingAndCategoryOrderByReviewCountDesc(search,search, category);
            }else {
                product = productRepository.findAllByNameContainingOrKeywordsKeywordContainingAndCategory(search,search, category);
            }
        List<Product> filteredProduct = new ArrayList<>();
        for (Product p : product) {
            if (p.getCategory().equals(category)) {
                filteredProduct.add(p);
            }
        }

        return filteredProduct;
    }
    @GetMapping("/category_all")
    @Operation(summary = "판매 상품 카테고리내 전체 검색 (최신순 or 조회순 or 리뷰순 or 가격순)", description = "Product 테이블에서 Name으로 검색하여 " +
            "선택한 Category의 정렬된 Product 객체 반환", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public List<Product> searchByCategoryAll(
            @RequestParam("Category") String category,
            @RequestParam("sortBy") String sortBy
    ) {
        List<Product> product;
        if (sortBy.equals("최신순")) {
            product = productRepository.findAllByCategoryOrderByCreatedAtDesc(category);
        }else if (sortBy.equals("가격순")) {
            product = productRepository.findAllByCategoryOrderByPrice(category);
        }else if (sortBy.equals("조회순")) {
            product = productRepository.findAllByCategoryOrderByViewCountDesc(category);
        }else if (sortBy.equals("리뷰순")) {
            product = productRepository.findAllByCategoryOrderByReviewCountDesc(category);
        }else {
            product = productRepository.findAllByCategory(category);
        }
        return product;
    }
    @GetMapping("/by_seller_id/{id}")
    @Operation(summary = "판매자의 모든 상품 찾기", description = "Product 테이블의 sellerId로 상품 반환", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @Transactional
    public List<Product> findBySellerId(@RequestParam("id") Long sellerId) {
        List<Product> product = productRepository.findBySellerId(sellerId);
        return product;
    }
}

