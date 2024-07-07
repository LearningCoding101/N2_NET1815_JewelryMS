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
    Date startDate;
    Date endDate;
    boolean status;

    @Column(name="discount")
    @Min(0)
    @Max(100)
    private Integer discount;

    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("promotion")
    private List<ProductSell_Promotion> productSellPromotions;

    @OneToMany(mappedBy = "promotion")
    @JsonIgnoreProperties
    private List<OrderDetail> orderDetails;
}