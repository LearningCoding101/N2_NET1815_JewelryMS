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

    //For testing purposes
    public <E> Customer(long l, String mail, String number, int i, boolean b, String male, Date date, String customer1, Object o, HashSet<E> es, Object o1) {
    }

    public String getLoyaltyRank() {
        int totalPoints = this.pointAmount;
        if (totalPoints >= 0 && totalPoints <= 99) {
            return "Connect";
        } else if (totalPoints >= 100 && totalPoints <= 399) {
            return "Member";
        } else if (totalPoints >= 400 && totalPoints <= 999) {
            return "Companion";
        } else if (totalPoints >= 1000) {
            return "Intimate";
        } else {
            return "Unknown";
        }
    }
}

