package LikeLion.UnderTheCBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "shopping_history")
public class ShoppingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "int")
    private Integer id;

    @Column(nullable = false, columnDefinition = "varchar(10)")
    private String status;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment paymentId;
}
