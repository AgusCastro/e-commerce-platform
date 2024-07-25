package acastro.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDER_ITEM")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ColumNames.ID)
    private Long id;

    @ManyToOne
    @JoinColumn( name = ColumNames.PRODUCT_ID, referencedColumnName = Product.ColumNames.ID)
    private Product product;

    @ManyToOne
    @JoinColumn( name = ColumNames.ORDER_ID, referencedColumnName = Order.ColumNames.ID)
    private Order order;

    @Column(name = ColumNames.QUANTITY)
    private Integer quantity;

    @Column(name = ColumNames.UNIT_PRICE)
    private Double unitPrice;

    public Double getTotal() {
        return this.unitPrice * this.quantity;
    }

    static final class ColumNames {
        public static final String ID = "ID";
        public static final String ORDER_ID = "ORDER_ID";
        public static final String PRODUCT_ID = "PRODUCT_ID";
        public static final String QUANTITY = "QUANTITY";
        public static final String UNIT_PRICE = "UNIT_PRICE";
    }

}
