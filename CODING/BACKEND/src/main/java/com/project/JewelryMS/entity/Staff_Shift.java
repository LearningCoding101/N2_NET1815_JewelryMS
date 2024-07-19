package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"staffAccount", "shift"})
@Table(name = "Staff_Shift")
public class Staff_Shift implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PK_StaffShift;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "staff_id", referencedColumnName = "PK_staffID")
    @JsonIgnoreProperties
    @JsonManagedReference
    @JsonBackReference
    private StaffAccount staffAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_id", referencedColumnName = "shiftID")
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonBackReference
    private Shift shift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workAreaId", referencedColumnName = "workAreaID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonBackReference
    @JsonManagedReference
    private WorkArea workArea;

    @Column(name = "attendance_status")
    private String attendanceStatus;
}