package LikeLion.UnderTheCBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "shopping_list")
public class ShoppingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "int")
    private Integer id;

    /**  아직 테이블이 안만들어져서 주석처리 */
//    @ManyToOne(cascade = CascadeType.REMOVE)
//    @JoinColumn(name="buyer_id", referencedColumnName = "id")
//    private Buyer buyerId;

//    @ManyToOne(cascade = CascadeType.REMOVE)
//    @JoinColumn(name="product_id", referencedColumnName = "id")
//    private Product ProductId;
}
