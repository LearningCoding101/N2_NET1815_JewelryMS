package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "WorkArea")
public class WorkArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "workAreaID", unique = true, nullable = false)
    private String workAreaID;

    @Column(name = "register")
    private int register;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "workArea", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Shift> shifts = new HashSet<>();

    @Column(name = "status")
    private String status = "Active"; // Default status is "Active"
}
