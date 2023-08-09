package LikeLion.UnderTheCBackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "product")
@Getter
@Setter

public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(length = 20)
    private Long id;

    private Long seller_id;

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

    @Column(length = 255)
    private String main_image; //image 저장방법 확인 후 수정필요함

    @Column(length = 255)
    private String keyword;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")//Date타입 포맷 변경
    private Date period;

    @Column(length = 255)
    private String category;

    @Column(name="view_count")
    private int viewCount;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")//Date타입 포맷 변경
    @Column(name="created_at")
    private Date createdAt;
}
