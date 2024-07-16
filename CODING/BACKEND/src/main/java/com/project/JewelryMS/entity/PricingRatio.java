package com.project.JewelryMS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="PricingRatio")
public class PricingRatio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    Float pricingRatioPS;
    Float pricingRatioPB;

    //For testing purposes
    public PricingRatio(long id, float pricingRatio) {
    }
}
