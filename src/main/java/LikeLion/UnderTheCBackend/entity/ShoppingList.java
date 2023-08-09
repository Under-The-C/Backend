package LikeLion.UnderTheCBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "shopping_list")
public class ShoppingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private Date created_at;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="buyer_id", referencedColumnName = "id")
    private Buyer buyerId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private Product ProductId;
}
