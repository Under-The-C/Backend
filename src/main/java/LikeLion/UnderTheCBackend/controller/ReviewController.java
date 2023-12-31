package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.entity.Product;
import LikeLion.UnderTheCBackend.entity.Review;
import LikeLion.UnderTheCBackend.entity.ReviewImage;
import LikeLion.UnderTheCBackend.repository.ReviewRepository;
import LikeLion.UnderTheCBackend.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@Tag(name = "Review API", description = "리뷰 API")
@RequestMapping("/api/v1/review")

public class  ReviewController {
    final String imagesPath = "/src/main/resources/images/";
    ReviewRepository reviewRepository;
    ProductRepository productRepository;
    ReviewController(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "리뷰 추가", description = "Review 테이블에 리뷰 추가", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public Review addByBuyerId(
            @RequestParam Long productId,
            @RequestParam int point,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile reviewImage,
            HttpServletRequest request
    ) throws IOException {
        // 로그인한 유저인지 확인
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }
        // 로그인한 유저의 id를 받아옴
        Long buyerId = (Long) session.getAttribute("user");

        Review newReview = new Review();
        newReview.setBuyerId(buyerId);
        newReview.setPoint(point);
        newReview.setProductId(productId);
        newReview.setDescription(description);
        newReview.setCreatedAt(new Date()); // 현재 시간 설정
        //리뷰이미지 추가
        if (reviewImage != null && !reviewImage.isEmpty()) {
            String filename = reviewImage.getOriginalFilename();
            log.info("reviewImage.getOriginalFilename = {}", filename);

            String absolutePath = System.getProperty("user.dir");;
            log.info(" absolutePath = {}", absolutePath);
            File Folder = new File(absolutePath+imagesPath);
            // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
            if (!Folder.exists()) {
                try{
                    Folder.mkdirs(); //폴더 생성합니다.
                    System.out.println("폴더가 생성되었습니다.");
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }else {
                System.out.println("이미 폴더가 생성되어 있습니다.");
            }
            reviewImage.transferTo(new File(absolutePath +imagesPath+filename));
            newReview.setReviewImage(filename);
        }

        Review savedReview = reviewRepository.save(newReview); // 리뷰 저장 후 반환

        // 해당 상품의 리뷰 추가 및 업데이트
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            List<Review> reviews = reviewRepository.findByProductId(productId);
            double totalPoints = product.getAverageReviewPoint() * reviews.size();
            totalPoints += point;
            product.setAverageReviewPoint(totalPoints / (reviews.size() + 1)); // 평균 업데이트
            product.setReviewCount(product.getReviewCount()+1);
            productRepository.save(product);
        }

        return savedReview;
    }
    @GetMapping("/view_all")
    @Operation(summary = "리뷰 모두 찾기", description = "Review 테이블의 모든 리뷰 반환", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public List<Review> findAll(){
        List<Review> review = null;
        review = reviewRepository.findAll();
        return review;
    }

    @GetMapping("/view_by_buyer/{buyerId}")
    @Operation(summary = "구매자가 작성한 리뷰 모두 찾기", description = "Review 테이블의 buyerId가 일치하는 리뷰 반환", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public List<Review> findAllByBuyerId(@RequestParam("buyerId") Long buyerId){
        List<Review> review = null;
        review = reviewRepository.findAllByBuyerId(buyerId);
        return review;
    }

    @GetMapping("/view_by_product/{productId}")
    @Operation(summary = "해당 상품의 리뷰 모두 찾기", description = "Review 테이블의 productId가 일치하는 리뷰 반환", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public List<Review> findAllByProductId(@RequestParam("productId") Long productId){
        List<Review> review = null;
        review = reviewRepository.findAllByProductId(productId);
        return review;
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "리뷰 삭제", description = "Review 테이블에 지정된 id로 리뷰 삭제", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public Review deleteById(@RequestParam("reviewId") Long reviewId, HttpServletRequest request) {
        // 로그인한 유저인지 확인
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }
        //String sellerId = seller.getId(); seller테이블 받으면 구현 재개

        Optional<Review> review = reviewRepository.findById(reviewId);

        if (review.isPresent()) {/* seller테이블 받으면 구현 재개
                if (!product.get().getSeller_id().equals(sellerId)){
                    throw new IllegalArgumentException("해당 상품의 판매자만 삭제할 수 있습니다.");
                }*/
            reviewRepository.deleteById(reviewId);
            return review.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 ID의 상품을 찾을 수 없습니다.");
        }
    }
}