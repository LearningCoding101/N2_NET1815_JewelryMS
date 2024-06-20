package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.entity.Staff_Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffShiftRepository extends JpaRepository<Staff_Shift, Long> {
    Optional<Staff_Shift> findByStaffAccountAndShift(StaffAccount staffAccount, Shift shift);
}
