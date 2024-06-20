package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Performance")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "staffAccount.performance"})
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long PK_performanceID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_staffID", referencedColumnName = "PK_staffID")
    @JsonIgnoreProperties
    private StaffAccount staffAccount;

    private LocalDateTime date;
    private int salesMade;
    private double revenueGenerated;
    private int customerSignUp;
    private boolean status = true;
}
