package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = {"staffShifts"})
@ToString(exclude = {"staffShifts"})
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

    @OneToMany(mappedBy = "shift", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Staff_Shift> staffShifts;
}