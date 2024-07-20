package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.entity.Staff_Shift;
import com.project.JewelryMS.entity.WorkArea;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StaffShiftRepository extends JpaRepository<Staff_Shift, Long> {
    Optional<Staff_Shift> findByStaffAccountAndShift(StaffAccount staffAccount, Shift shift);

    List<Staff_Shift> findAllByStaffAccountAndShift_StartTimeBetween(
            StaffAccount staffAccount,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    @Query("SELECT ss FROM Staff_Shift ss WHERE ss.staffAccount.account.PK_userID = :userID AND ss.shift.startTime <= :now AND ss.shift.endTime >= :now")
    List<Staff_Shift> findActiveShiftsForStaff(@Param("userID") Long userID, @Param("now") LocalDateTime now);

    @Query("SELECT ss FROM Staff_Shift ss WHERE ss.shift.startTime < :now AND ss.attendanceStatus = 'Not yet'")
    List<Staff_Shift> findPastShifts(@Param("now") LocalDateTime now);

    @Query("SELECT ss FROM Staff_Shift ss WHERE ss.workArea.id = :workAreaId")
    List<Staff_Shift> findByWorkAreaId(@Param("workAreaId") Long workAreaId);

    @Query("SELECT ss FROM Staff_Shift ss WHERE ss.workArea = :workArea")
    List<Staff_Shift> findByWorkArea(WorkArea workArea);

    @Query("SELECT sa FROM StaffAccount sa WHERE sa.staffID = :staffID")
    StaffAccount findStaffAccountById(@Param("staffID") Integer staffID);

}
