package LikeLion.UnderTheCBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "shopping_history")
public class ShoppingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NonNull
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private Buyer buyerId;

    @NonNull
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product productId;

    @NonNull
    @Column(nullable = false, columnDefinition = "varchar(10)")
    private String status;

    @Column(name = "imp_uid", nullable = false, columnDefinition = "varchar(20)")
    private String impUid;

    @CreatedDate
    @Column(updatable = false)
    private Date created_at;

    @LastModifiedDate
    private Date updated_at;
}
