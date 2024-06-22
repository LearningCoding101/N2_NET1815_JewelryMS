package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Staff")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "account.staffAccount", "shift.staffAccounts"})
public class StaffAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_staffID")
    private int staffID;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_UserID", referencedColumnName = "PK_userID")
//    @JsonBackReference
    @JsonIgnoreProperties
    private Account account;


    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "salary")
    private float salary;

    @Column(name = "startDate")
    private Date startDate;

    @OneToMany(mappedBy = "staffAccount")
    @JsonIgnoreProperties
    @JsonManagedReference
    Set<Staff_Shift> staffShifts;

    @OneToMany(mappedBy = "staffAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties
    Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    @OneToMany(mappedBy = "staffAccount")
    @JsonIgnoreProperties("staffAccount")
    List<Customer> customer;


}