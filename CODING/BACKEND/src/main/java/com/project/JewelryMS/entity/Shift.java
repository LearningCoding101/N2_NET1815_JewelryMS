package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Shift")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shiftID")
    private int shiftID;

    @Column(name = "endTime")
    private LocalDateTime endTime;

    @Column(name = "shiftType")
    private String shiftType;

    @Column(name = "startTime")
    private LocalDateTime startTime;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workAreaId", referencedColumnName = "workAreaID")
    @JsonManagedReference
    private WorkArea workArea;

    @OneToMany(mappedBy = "shift", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Staff_Shift> staffShifts;
}
