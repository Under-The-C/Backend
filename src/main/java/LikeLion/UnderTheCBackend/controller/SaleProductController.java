package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.dto.AddProduct;
import LikeLion.UnderTheCBackend.dto.ResponseProduct;
import LikeLion.UnderTheCBackend.entity.*;
import LikeLion.UnderTheCBackend.repository.ProductRepository;
import LikeLion.UnderTheCBackend.repository.UserRepository;
import LikeLion.UnderTheCBackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static LikeLion.UnderTheCBackend.entity.Role.BUYER;


@RestController
@Slf4j
@Tag(name = "SaleProduct API", description = "상품 판매 API")
@RequestMapping("/api/v1/sale_product")

public class  SaleProductController {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final UserService userService;
    private final static String IMAGE_PATH = "/src/main/resources/images/";

    @Autowired
    SaleProductController(ProductRepository productRepository, UserRepository userRepository, UserService userService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    private boolean mkdir(String path) {
        String absolutePath = System.getProperty("user.dir");
        File Folder = new File(absolutePath + path);
        // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
        if (!Folder.exists()) {
            try {
                Folder.mkdirs(); //폴더 생성합니다.
                System.out.println("폴더가 생성되었습니다.");
                return true;
            }
            catch(Exception e){
                e.getStackTrace();
                return false;
            }
        } else {
            System.out.println("이미 폴더가 생성되어 있습니다.");
            return true;
        }
    }

    @PostMapping(value="/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "판매 상품 추가", description = "Product 테이블에 상품 정보 추가", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public String addByProductName(@ModelAttribute AddProduct data, HttpServletRequest request) throws IOException {
        //로그인 정보 왜래키로 받아 Product테이블의 seller_id에 set 필요
        //로그인 정보 예외처리 필요

        // 로그인한 유저인지 확인
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }
        Long sellerId = (Long) session.getAttribute("user");
        User user = userRepository.findById(sellerId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "가입 되어 있지 않습니다.");
        }

        List<String> keyword = data.getKeyword();
        MultipartFile mainImage = data.getMainImage();
        List<MultipartFile> detailImage = data.getDetailImage();

        //같은 이름의 상품은 생성 불가
        Optional<Product> existingProduct = productRepository.findByName(data.getName());
        if (existingProduct.isPresent()) {
            // 이미 해당 이름의 상품이 존재하는 경우 예외 처리
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "같은 이름의 상품이 이미 존재합니다.");
        }
        Product newProduct = new Product();
        newProduct.setUserId(user);
        newProduct.setName(data.getName());
        newProduct.setSubTitle(data.getSubTitle());
        newProduct.setPrice(new BigDecimal(data.getPrice()));
        newProduct.setDescription(data.getDescription());
        newProduct.setSubDescription(data.getSubDescription());
        newProduct.setMainImage(mainImage.getOriginalFilename());

        List<ProductKeywordConnect> keywordEntities = new ArrayList<>();
        for (String keywordStr : keyword) {
            ProductKeywordConnect keywords = new ProductKeywordConnect();
            keywords.setKeyword(keywordStr);
            keywordEntities.add(keywords);
        }
        newProduct.setKeywords(keywordEntities);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yy_MM_dd");
        String absolutePath = System.getProperty("user.dir");;

        //메인이미지 파일 저장
        if (mainImage != null && !mainImage.isEmpty()) {
            String filename = mainImage.getOriginalFilename();
            log.info("mainImage.getOriginalFilename = {}", filename);
            log.info(" absolutePath = {}", absolutePath);

            // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
            if (mkdir(IMAGE_PATH)) {
                log.info("폴더가 생성되었습니다.");
            } else {
                log.info("폴더가 생성되지 않았습니다.");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "폴더가 생성되지 않았습니다.");
            }

            String randomStr = formatter.format(date) + UUID.randomUUID();
            newProduct.setMainImage(randomStr + filename);
            mainImage.transferTo(new File(absolutePath + IMAGE_PATH + newProduct.getMainImage()));
        }

        // 이미지 URL 리스트에서 ReviewImage 엔티티를 생성합니다.
        List<ProductDetailImage> detailImages = new ArrayList<>();
        for (MultipartFile image : detailImage) {
            ProductDetailImage productDetailImage = new ProductDetailImage();
            String randomStr = formatter.format(date) + UUID.randomUUID();
            productDetailImage.setImageUrl(randomStr + image.getOriginalFilename());
            detailImages.add(productDetailImage);
            image.transferTo(new File(absolutePath + IMAGE_PATH + productDetailImage.getImageUrl()));
        }
        newProduct.setDetailImage(detailImages);
        newProduct.setSaleStartDate(data.getSaleStartDate());
        newProduct.setSaleEndDate(data.getSaleEndDate());
        newProduct.setCategory(data.getCategory());
        newProduct.setViewCount(0); // 초기 viewCount 설정
        newProduct.setCreatedAt(date); // 현재 시간 설정

        productRepository.save(newProduct);
        return "success";
    }

    @GetMapping("/view") //조회수
    @Operation(summary = "판매 상품 찾기", description = "Product 테이블의 id로 특정 상품 반환", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public ResponseEntity<ResponseProduct> findById(@RequestParam("id") Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setViewCount(product.getViewCount() + 1); // viewCount를 1 더해줌
            productRepository.save(product); // 변경된 상품 정보 저장

            ResponseProduct responseProduct = convertToResponseProduct(product);
            return ResponseEntity.ok(responseProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseProduct convertToResponseProduct(Product product) {
        return new ResponseProduct(
                product.getId(),
                product.getUserId().getId(),
                product.getName(),
                product.getSubTitle(),
                product.getPrice(),
                product.getDescription(),
                product.getSubDescription(),
                product.getMainImage(),
                product.getDetailImage().stream()
                        .map(ProductDetailImage::getImageUrl)
                        .collect(Collectors.toList()),
                product.getKeywords().stream()
                        .map(ProductKeywordConnect::getKeyword)
                        .collect(Collectors.toList()),
                product.getSaleStartDate(),
                product.getSaleEndDate(),
                product.getCategory(),
                product.getViewCount(),
                product.getReviewCount(),
                product.getAverageReviewPoint(),
                product.getCreatedAt()
        );
    }
    @GetMapping("/view_all")
    @Operation(summary = "판매 상품 모두 찾기", description = "Product 테이블의 모든 상품 반환", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })@Transactional
    public List<Product> findAll(){
        List<Product> product = null;
        product = productRepository.findAll();
        return product;
    }


    @PatchMapping("/update/{id}")
    @Operation(summary = "판매 상품 수정", description = "product table의 id 입력받아 판매 상품 수정", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public String updateProduct(@PathVariable("id") Long productId, @ModelAttribute AddProduct data, HttpServletRequest request) throws IOException {
        // 로그인한 유저인지 확인
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // 날짜 설정
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yy_MM_dd");
            String absolutePath = System.getProperty("user.dir");;

            // 판매 상품 객체에 productRequest로부터 값을 업데이트합니다.
            if (data.getName() != null) {
                Optional<Product> existingProduct = productRepository.findByName(data.getName());
                if (existingProduct.isPresent() && !product.getId().equals(existingProduct.get().getId())) {
                    // 이미 해당 이름의 상품이 존재하는 경우 예외 처리
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "같은 이름의 상품이 이미 존재합니다.");
                }
                product.setName(data.getName());
            }
            if (data.getCategory() != null)
                product.setCategory(data.getCategory());
            if (data.getPrice() != null)
                product.setPrice(new BigDecimal(data.getPrice()));
            if (data.getKeyword() != null) {
                List<ProductKeywordConnect> updatedKeywords = new ArrayList<>();
                for (String keywordStr : data.getKeyword()) {
                    ProductKeywordConnect keywordEntity = new ProductKeywordConnect();
                    keywordEntity.setKeyword(keywordStr);
                    updatedKeywords.add(keywordEntity);
                }
                product.setKeywords(updatedKeywords);
            }
            if (data.getDetailImage() != null) {
                // 기존 상품 이미지 삭제
                for (ProductDetailImage productDetailImage : product.getDetailImage()) {
                    String srcFileName = productDetailImage.getImageUrl();
                    if (srcFileName != null) {
                        File file = new File(absolutePath + IMAGE_PATH + srcFileName);
                        file.delete();
                    }
                }
                List<ProductDetailImage> updatedDetailImages = new ArrayList<>();
                for (MultipartFile imageUrl : data.getDetailImage()) {
                    ProductDetailImage productDetailImage = new ProductDetailImage();
                    String randomStr = formatter.format(date) + UUID.randomUUID();
                    productDetailImage.setImageUrl(randomStr + imageUrl.getOriginalFilename());
                    updatedDetailImages.add(productDetailImage);
                    imageUrl.transferTo(new File(absolutePath + IMAGE_PATH + productDetailImage.getImageUrl()));
                }
                product.setDetailImage(updatedDetailImages);
            }
            if (data.getDescription() != null)
                product.setDescription(data.getDescription());
            if (data.getSaleStartDate() != null)
                product.setSaleStartDate(data.getSaleStartDate());
            if (data.getSaleEndDate() != null)
                product.setSaleEndDate(data.getSaleEndDate());
            if (data.getSubTitle() != null)
                product.setSubTitle(data.getSubTitle());
            if (data.getSubDescription() != null)
                product.setSubDescription(data.getSubDescription());
            MultipartFile mainImage = data.getMainImage();
            if (mainImage != null) {
                // 기존 메인 이미지 삭제
                String srcFileName = product.getMainImage();
                if (srcFileName != null) {
                    File file = new File(absolutePath + IMAGE_PATH + srcFileName);
                    file.delete();
                }
                product.setMainImage(mainImage.getOriginalFilename());
                mainImage.transferTo(new File(absolutePath + IMAGE_PATH + product.getMainImage()));
            }
            productRepository.save(product);
            return "success";
        }else {
            //예외처리
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 ID의 상품을 찾을 수 없습니다.");
        }
    }
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "판매 상품 삭제", description = "Product 테이블에 지정된 id로 판매 상품 삭제", responses = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public String deleteById(@PathVariable("id") Long productId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }
        Long userId = (Long) session.getAttribute("user");
        if (userId != productId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 상품의 판매자만 삭제할 수 있습니다.");
        }
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 ID의 상품을 찾을 수 없습니다."));
        productRepository.delete(product);
        return "success";
    }

    @PostConstruct
    public void init() {

        for (int i=1; i<=10; ++i) {
            userService.createUser("user"+i, "010-1234-5678"+i, "user"+i, "서울시 강남구"+i, "서울시 강남구"+i, BUYER, "certificate"+i);
            Product product = new Product();
            User user = userRepository.findById((long) i).orElse(null);
            product.setUserId(user);
            product.setName("상품" + i);
            product.setSubTitle("소제목" + i);
            product.setPrice(new BigDecimal(1000 + i));
            product.setDescription("설명" + i);
            product.setSubDescription("추가설명" + i);
            product.setMainImage("이미지" + i);
            product.setSaleStartDate(new Date());
            product.setSaleEndDate(new Date());
            product.setCategory("카테고리" + i);
            product.setViewCount(0);
            product.setCreatedAt(new Date());

            ProductKeywordConnect productKeywords = new ProductKeywordConnect();
            ProductKeyword productKeyword = new ProductKeyword();
            productKeyword.setKeyword("키워드" + i);

            productKeywords.setProductKeyword(productKeyword);
            productKeywords.setProduct(product);

            productRepository.save(product);
        }
    }
}

