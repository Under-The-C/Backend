package LikeLion.UnderTheCBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "int")
    private Integer id;

    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String name;

    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String call_number;

    @Column(nullable = false, columnDefinition = "varchar(30)")
    private String address;

    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String method;

    @CreatedDate
    @Column(updatable = false)
    private Date created_at;

    /**  아직 테이블이 안만들어져서 주석처리 */
//    @ManyToOne(cascade = CascadeType.REMOVE)
//    @JoinColumn(name="buyer_id", referencedColumnName = "id")
//    private Buyer buyerId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private Product productId;
}
