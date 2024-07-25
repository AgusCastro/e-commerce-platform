package acastro.ecommerce.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import acastro.ecommerce.enums.OrderStatus;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ColumNames.ID)
    private Long id;

    @Column(name = ColumNames.CREATION_DATE, updatable = false, nullable = false)
    private LocalDate creationDate;

    @Column(name = ColumNames.DELIVERY_DATE)
    private LocalDate deliveryDate;

    @Column(name = ColumNames.DELIVERY_ADDRESS, nullable = false, length = 250)
    private String deliveryAddress;

    @Column(name = ColumNames.CUSTOMER_NAME, nullable = false, length = 100)
    private String customerName;

    @Column(name = ColumNames.STATUS, nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items = List.of();

    public Order(String customerName, String deliveryAddress) {
        this.customerName = customerName;
        this.deliveryAddress = deliveryAddress;
        this.status = OrderStatus.PENDING;
        this.creationDate = LocalDate.now();
        this.items = List.of();
    }

    static final class ColumNames {
        public static final String ID = "ID";
        public static final String CREATION_DATE = "CREATION_DATE";
        public static final String DELIVERY_DATE = "DELIVERY_DATE";
        public static final String DELIVERY_ADDRESS = "DELIVERY_ADDRESS";
        public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
        public static final String STATUS = "STATUS";
    }
}
