package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.entity.Product;
import LikeLion.UnderTheCBackend.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

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
            "정렬된 Product 객체 반환 (조회, 리뷰순 추가 예정)", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public List<Product> searchAll(
            @RequestParam("Name") String productName,
            //@RequestParam("Category") String category,
            @RequestParam("sortBy") String sortBy
    ) {
        // **조회순, 리뷰순 정렬을 위해 조회, 리뷰 카운트 값 테이블에 추가 필요함**
        List<Product> product;
        if (sortBy.equals("최신순")) {
            product = productRepository.findAllByNameContainingOrderByCreatedAtDesc(productName);
        }else if (sortBy.equals("가격순")){
            product = productRepository.findAllByNameContainingOrderByPrice(productName);
        }else{
            product = productRepository.findAllByNameContaining(productName);
        }
        return product;
    }/*
    @GetMapping("/category")
    @Operation(summary = "모든 판매 상품 검색 (최신순 or 조회순 or 리뷰순 or 가격순)", description = "Product 테이블에서 Name으로 검색하여 " +
            "선택한 Category의 정렬된 Product 객체 반환", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public List<Product> searchByCategory(
            @RequestParam("Name") String productName,
            @RequestParam("Category") String category,
            @RequestParam("sortBy") String sortBy
    ) {
        // **조회순, 리뷰순 정렬을 위해 조회, 리뷰 카운트 값 테이블에 추가 필요해보임**
        List<Product> product;
        if (sortBy.equals("최신순")) {
            if (category !=null && !category.isEmpty()) {
                product = productRepository.findAllByNameAndCategoryAndOrderByCreated_atDesc(productName,category,sortBy);
            } else {
                product = productRepository.findAllByNameAndOrderByCreated_atDesc(productName, sortBy);
            }
        }else if (sortBy.equals("가격순")){
            product = productRepository.findByNameAndOrderByPrice(sortBy, sortBy);
        }else{
            product = productRepository.findAll();
        }
        return product;
    }*/
}
