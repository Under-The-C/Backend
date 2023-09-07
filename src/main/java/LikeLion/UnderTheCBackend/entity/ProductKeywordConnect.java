package LikeLion.UnderTheCBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_keyword_connect")
@Getter
@Setter
public class ProductKeywordConnect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="product_id",referencedColumnName = "id")
    private Product product;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="producKeyword_id", referencedColumnName = "id")
    private ProductKeyword productKeyword;

    @Column(unique = true)
    private String keyword;
}