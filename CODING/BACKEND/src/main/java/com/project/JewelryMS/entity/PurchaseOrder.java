package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@JsonIgnoreProperties("orderDetails")
public class PurchaseOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long PK_OrderID;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<OrderDetail> orderDetails = new HashSet<>();

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("orderBuyDetails")

    Set<OrderBuyDetail> orderBuyDetails = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_CustomerID", referencedColumnName = "PK_CustomerID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_StaffID", referencedColumnName = "PK_staffID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonManagedReference
    @JsonBackReference
    private StaffAccount staffAccount;

    String paymentType;
    Float totalAmount;
    String email;
    Date purchaseDate;
    //1: not pay yet, 2: in process , 3 : paid
    Integer status;
    @Column(name="imageConfirm")
    String image;
    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "PK_OrderID=" + PK_OrderID +
                ", paymentType='" + paymentType + '\'' +
                ", totalAmount=" + totalAmount +
                ", purchaseDate=" + purchaseDate +
                ", status=" + status +
                '}';
    }
}
