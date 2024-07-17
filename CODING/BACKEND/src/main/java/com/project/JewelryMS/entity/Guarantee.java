package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Guarantee")
@ToString(exclude = "productSell")

@EqualsAndHashCode(exclude = "productSell")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "productSell.guarantee"})
public class Guarantee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long PK_guaranteeID;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_productID", referencedColumnName = "PK_productID")
    @JsonIgnoreProperties
    @JsonBackReference

    private ProductSell productSell;

    //For years, or months
    private String policyType;

    //For what product
    private String coverage;
    @Column(name="warrantyPeriodMonth")
    private Integer warrantyPeriodMonth;
    private boolean status = true;
    @Override
    public int hashCode() {
        return Objects.hash(PK_guaranteeID, policyType, coverage, warrantyPeriodMonth, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guarantee guarantee = (Guarantee) o;
        return PK_guaranteeID == guarantee.PK_guaranteeID &&
                status == guarantee.status &&
                Objects.equals(policyType, guarantee.policyType) &&
                Objects.equals(coverage, guarantee.coverage) &&
                Objects.equals(warrantyPeriodMonth, guarantee.warrantyPeriodMonth);
    }
}
