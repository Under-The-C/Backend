package LikeLion.UnderTheCBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private Integer id;

    private int seller_id;

    @Column(length = 255)
    private String name;

    @Column(columnDefinition = "decimal(64,0)")
    private BigDecimal price;

    @Column(length = 255)
    private String description;

    @Column(length = 255)
    private String main_image; //image 저장방법 확인 후 수정필요함

    @Column(length = 255)
    private String keyword;

    private Date period;

    @Column(length = 255)
    private String category;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;
}
