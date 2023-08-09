package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.entity.Product;
import LikeLion.UnderTheCBackend.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "SaleProduct API", description = "상품 판매 API")
@RequestMapping("/api/v1/sale_product")

public class  SaleProductController {
    ProductRepository productRepository;

    SaleProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
        @PostMapping("/add")
        @Operation(summary = "판매 상품 추가", description = "Product 테이블에 상품 정보 추가", responses = {
                @ApiResponse(responseCode = "200", description = "성공")
        })
        public Product addByProductnName(@RequestBody Product productRequest) {
            //로그인 정보 왜래키로 받아 Product테이블의 seller_id에 set 필요
            //로그인 정보 예외처리 필요


            //검색 조건으로 사용할 productName
            String productName = productRequest.getName();
            //같은 이름의 상품은 생성 불가
            Optional<Product> existingProduct = productRepository.findByName(productName);
            if (existingProduct.isPresent()) {
                // 이미 해당 이름의 상품이 존재하는 경우 예외 처리
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "같은 이름의 상품이 이미 존재합니다.");
            }
            productRepository.save(productRequest);
            return productRequest;
        }

            @GetMapping("/view") //조회수
            @Operation(summary = "판매 상품 찾기", description = "Product 테이블의 id로 특정 상품 반환", responses = {
                    @ApiResponse(responseCode = "200", description = "성공")
            })
            public Optional<Product> findById(@RequestParam("id") Long productId) {
                Optional<Product> product = null;
                product = productRepository.findById(productId);
                return product;
            }
            @GetMapping("/view_all")
            @Operation(summary = "판매 상품 모두 찾기", description = "Product 테이블의 모든 상품 반환", responses = {
                    @ApiResponse(responseCode = "200", description = "성공")
            })
            public List<Product> findAll(){
                List<Product> product = null;
                product = productRepository.findAll();
                return product;
            }

    @PatchMapping("/update/{id}")
    @Operation(summary = "판매 상품 수정", description = "product table의 id 입력받아 판매 상품 수정", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public Product updateProduct(
            @PathVariable("id") Long productId,
            @RequestBody Product productRequest
    ) {

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // 판매 상품 객체에 productRequest로부터 값을 업데이트합니다.
            if (productRequest.getName() != null) {
                String productName = productRequest.getName();
                Optional<Product> existingProduct = productRepository.findByName(productName);
                if (existingProduct.isPresent()) {
                    // 이미 해당 이름의 상품이 존재하는 경우 예외 처리
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "같은 이름의 상품이 이미 존재합니다.");
                }
                product.setName(productRequest.getName());
            }
            if (productRequest.getCategory() != null)
                product.setCategory(productRequest.getCategory());
            if (productRequest.getPrice() != null)
                product.setPrice(productRequest.getPrice());
            if (productRequest.getKeyword() !=null)
                product.setKeyword(productRequest.getKeyword());
            if (productRequest.getDescription() !=null)
                product.setDescription(productRequest.getDescription());
            if (productRequest.getPeriod() !=null)
                product.setPeriod(productRequest.getPeriod());
            if (productRequest.getMain_image()!=null) //이미지 관련 DB접근은 수정 필요함
                product.setMain_image(product.getMain_image());
            productRepository.save(product);
            return product;
        } else {
            //예외처리
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 ID의 상품을 찾을 수 없습니다.");
        }
    }
        @DeleteMapping("/delete/{id}")
        @Operation(summary = "판매 상품 삭제", description = "Product 테이블에 지정된 id로 판매 상품 삭제", responses = {
                @ApiResponse(responseCode = "200", description = "성공")
        })
        public Product deleteById(@RequestParam("evaluationId") Long productId) {

            //String sellerId = seller.getId(); seller테이블 받으면 구현 재개

            Optional<Product> product = productRepository.findById(productId);

            if (product.isPresent()) {/* seller테이블 받으면 구현 재개
                if (!product.get().getSeller_id().equals(sellerId)){
                    throw new IllegalArgumentException("해당 상품의 판매자만 삭제할 수 있습니다.");
                }*/
                productRepository.deleteById(productId);
                return product.get();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 ID의 상품을 찾을 수 없습니다.");
            }
        }
    }

