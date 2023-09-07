package LikeLion.UnderTheCBackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "shopping_list")
public class ShoppingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private Date created_at;

    @NonNull
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User userId;

    @NonNull
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private Product productId;

    @NonNull
    @Column(nullable = false)
    private Integer count;
}
