package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Staff")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StaffAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_staffID")
    private int staffID;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_UserID", referencedColumnName = "PK_userID")
    private Account account;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "salary")
    private float salary;

    @Column(name = "startDate")
    private LocalDateTime startDate;

    @OneToMany(mappedBy = "staffAccount", fetch = FetchType.EAGER) // Ensure eager fetching here
    @JsonIgnoreProperties("staffAccount")
    private Set<Staff_Shift> staffShifts;

    @OneToMany(mappedBy = "staffAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("staffAccount")
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    @OneToMany(mappedBy = "staffAccount")
    @JsonIgnoreProperties("staffAccount")
    private List<Customer> customer;
}

