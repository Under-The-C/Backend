package LikeLion.UnderTheCBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

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

    @CreatedDate
    @Column(updatable = false)
    private Date created_at;

    @LastModifiedDate
    private Date updated_at;
}
