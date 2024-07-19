package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Shift;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    // Find shifts by start time
    @Query("SELECT s FROM Shift s WHERE s.startTime = :startTime")
    List<Shift> findByStartTime(@Param("startTime") LocalDateTime startTime);

    // Find shifts by end time
    @Query("SELECT s FROM Shift s WHERE s.endTime = :endTime")
    List<Shift> findByEndTime(@Param("endTime") LocalDateTime endTime);

    // Find shifts by shift type
    @Query("SELECT s FROM Shift s WHERE s.shiftType = :shiftType")
    List<Shift> findByShiftType(@Param("shiftType") String shiftType);

    // Find shifts by status
    @Query("SELECT s FROM Shift s WHERE s.status = :status")
    List<Shift> findByStatus(@Param("status") String status);

    @Query("SELECT s FROM Shift s WHERE DATE(s.startTime) = :date AND s.shiftType = :shiftType")
    Optional<Shift> findByDateAndType(@Param("date") LocalDate date, @Param("shiftType") String shiftType);
    // Find shifts by date and type
    @Query("SELECT s FROM Shift s WHERE FUNCTION('DATE', s.startTime) = :date AND s.shiftType = :shiftType")
    List<Shift> findAllByDateAndType(@Param("date") LocalDate date, @Param("shiftType") String shiftType);

    @Query("SELECT s FROM Shift s WHERE s.staffShifts IS EMPTY")
    List<Shift> findShiftsWithoutStaff();

    @Query("SELECT s FROM Shift s WHERE s.startTime < :cutoffDate")
    List<Shift> findShiftsOlderThan(@Param("cutoffDate") LocalDate cutoffDate);

    List<Shift> findAllByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Count the total number of shifts worked by a specific staff member
    @Query("SELECT COUNT(s) FROM Shift s JOIN s.staffShifts ss WHERE ss.staffAccount.account.email = :staffEmail")
    long countShiftsByStaffEmail(@Param("staffEmail") String staffEmail);

    @Query("SELECT COUNT(s) FROM Shift s JOIN s.staffShifts ss WHERE ss.staffAccount.account.email = :staffEmail AND s.startTime BETWEEN :startDate AND :endDate")
    long countShiftsByStaffEmailAndDateRange(@Param("staffEmail") String staffEmail, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}