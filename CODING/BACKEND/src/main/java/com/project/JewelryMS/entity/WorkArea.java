package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"staffAccounts"})
@ToString(exclude = {"staffAccounts"})
@Entity
@Table(name = "WorkArea")
public class WorkArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_WorkAreaID")
    private Long id; // Change id to PK_WorkAreaID

    @Column(name = "workAreaCode", unique = true, nullable = false)
    private String workAreaCode; // Change workAreaID to workAreaCode

    @Column(name = "register")
    private int register;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "workArea", fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnoreProperties("workArea")
    private Set<StaffAccount> staffAccounts = new HashSet<>();

    @Column(name = "status")
    private String status = "Active"; // Default status is "Active"
}
