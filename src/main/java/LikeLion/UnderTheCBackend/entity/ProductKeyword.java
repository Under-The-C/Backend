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
@Table(name = "product_keyword")
@Getter
@Setter
public class ProductKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 20)
    private Long id;

    @OneToMany(mappedBy = "productKeyword", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ProductKeywordConnect> products = new ArrayList<>();

    @Column(unique = true)
    private String keyword;
}