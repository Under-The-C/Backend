package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.entity.Product;
import LikeLion.UnderTheCBackend.entity.Review;
import LikeLion.UnderTheCBackend.entity.ReviewImage;
import LikeLion.UnderTheCBackend.repository.ReviewRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Review API", description = "리뷰 API")
@RequestMapping("/api/v1/review")

public class  ReviewController {
    ReviewRepository reviewRepository;

    ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @PostMapping("/add")
    @Operation(summary = "리뷰 추가", description = "Review 테이블에 리뷰 추가", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public Review addByBuyerId(
            @RequestParam Long buyerId,
            @RequestParam int point,
            @RequestParam String description,
            @RequestParam List<String> reviewImageUrls
    ){
        Review newReview = new Review();
        newReview.setBuyer_id(buyerId);
        newReview.setPoint(point);
        newReview.setDescription(description);

        // 이미지 URL 리스트에서 ReviewImage 엔티티를 생성합니다.
        List<ReviewImage> reviewImages = new ArrayList<>();
        for (String imageUrl : reviewImageUrls) {
            ReviewImage reviewImage = new ReviewImage();
            reviewImage.setImageUrl(imageUrl);
            reviewImages.add(reviewImage);
        }
        newReview.setReviewImage(reviewImages);

        newReview.setCreatedAt(new Date()); // 현재 시간 설정

        reviewRepository.save(newReview);
        return newReview;
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
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "리뷰 삭제", description = "Review 테이블에 지정된 id로 리뷰 삭제", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public Review deleteById(@RequestParam("reviewId") Long reviewId) {

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