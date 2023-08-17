package LikeLion.UnderTheCBackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(length = 20)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User userId;

    @Column(length = 255)
    private String name;

    @Column(length = 255, name="sub_title")//소제목
    private String subTitle;

    @Column(columnDefinition = "decimal(64,0)")
    private BigDecimal price;

    @Column(length = 255)
    private String description;

    @Column(length = 255, name="sub_description")//추가 설명
    private String subDescription;

    @Column(length = 255, name="main_image")
    private String mainImage; //image 저장방법 확인 후 수정필요함

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "detail_image")
    private List<ProductDetailImage> detailImage;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_keyword")
    private List<ProductKeywordConnect> keywords = new ArrayList<>();

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")//Date타입 포맷 변경
    private Date saleStartDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")//Date타입 포맷 변경
    private Date saleEndDate;

    @Column(length = 255)
    private String category;

    @Column(name="view_count")
    private int viewCount =0;

    @Column(name="review_count") //**추가된 요소**
    private int reviewCount =0;

    @Column(name = "average_review_point")
    private Double averageReviewPoint = 0.0;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")//Date타입 포맷 변경
    @Column(name="created_at")
    private Date createdAt;
}
