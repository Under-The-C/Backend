package LikeLion.UnderTheCBackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "review")
@Getter
@Setter
public class Review {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(length = 20)
    private Long id;

    @Column(name="buyer_id")
    private Long buyerId;

    private Long productId;//**추가된 요소**

    @Column(length = 20)
    private int point=0;

    @Column(length = 255)
    private String description;


    @Column(name = "review_Image")
    private String reviewImage; //한장으로 변경

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")//Date타입 포맷 변경
    @Column(name="created_at")
    private Date createdAt;
}
