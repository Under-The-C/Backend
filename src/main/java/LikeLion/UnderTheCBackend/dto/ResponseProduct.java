package LikeLion.UnderTheCBackend.dto;

import LikeLion.UnderTheCBackend.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class ResponseProduct {
    private Long id;
    private Long userId;
    private String name;
    private String subTitle;
    private BigDecimal price;
    private String description;
    private String subDescription;
    private String mainImage;
    private List<String> detailImages;
    private List<String> keywords;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")//Date타입 포맷 변경
    private Date saleStartDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")//Date타입 포맷 변경
    private Date saleEndDate;
    private String category;
    private int viewCount;
    private int reviewCount;
    private Double averageReviewPoint;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")//Date타입 포맷 변경
    private Date createdAt;
}