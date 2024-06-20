package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Staff_Shift")
public class Staff_Shift implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long PK_StaffShift;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "staff_id", referencedColumnName = "PK_staffID")
    @JsonBackReference
    StaffAccount staffAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_id", referencedColumnName = "shiftID")
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonBackReference
    Shift shift;

//    @Transient
//    private int staffId;
//
//    @Transient
//    private int shiftId;
//
//    @PostLoad
//    void fillTransient() {
//        if (staffAccount != null) {
//            this.staffId = staffAccount.getStaffID();
//        }
//        if (shift != null) {
//            this.shiftId = shift.getShiftID();
//        }
//    }
}

