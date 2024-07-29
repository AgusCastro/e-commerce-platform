package acastro.ecommerce.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ColumNames.ID)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = ColumNames.NAME, nullable = false, length = 40)
    @EqualsAndHashCode.Include
    private String name;

    @Column(name = ColumNames.DESCRIPTION, length = 100)
    @EqualsAndHashCode.Include
    private String description;

    @Column(name = ColumNames.PRICE, nullable = false, precision = 2)
    @EqualsAndHashCode.Include
    private BigDecimal price;

    @Column(name = ColumNames.STOCK, nullable = false)
    @EqualsAndHashCode.Include
    private Integer stock;

    @Column(name = ColumNames.DISCONTINUED, columnDefinition = "BOOL DEFAULT false NOT NULL")
    @EqualsAndHashCode.Include
    private boolean discontinued;

    static final class ColumNames {
        public static final String ID = "ID";
        public static final String NAME = "NAME";
        public static final String DESCRIPTION = "DESCRIPTION";
        public static final String PRICE = "PRICE";
        public static final String STOCK = "STOCK";
        public static final String DISCONTINUED = "DISCONTINUED";
    }
}
