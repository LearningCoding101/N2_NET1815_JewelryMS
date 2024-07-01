package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Promotion")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "productSell"})
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long PK_promotionID;
    String code;
    String description;
    LocalDateTime startDate;
    LocalDateTime endDate;
    boolean status;

    @Column(name="discount")
    @Min(0)
    @Max(100)
    private Integer discount;

    @ManyToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    private List<ProductSell> productSell ;

    @OneToMany(mappedBy = "promotion")
    @JsonIgnoreProperties
    private List<OrderDetail> orderDetails;
}