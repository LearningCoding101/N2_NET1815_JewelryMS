package com.project.JewelryMS.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_CustomerID")
    long PK_CustomerID;

    String email;
    String phoneNumber;
    int pointAmount;
    boolean status = true;
    @Column(name="gender")
    String gender;
    @Column(name="createDate")
    Date createDate;

    @Column(name="customerName")
    String cusName;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties
    Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_StaffID",referencedColumnName = "PK_staffID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    StaffAccount staffAccount;
}

