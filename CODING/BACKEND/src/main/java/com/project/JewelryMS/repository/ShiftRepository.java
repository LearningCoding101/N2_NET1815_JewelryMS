package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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

    // Find shifts by register
    @Query("SELECT s FROM Shift s WHERE s.register = :register")
    List<Shift> findByRegister(@Param("register") int register);

    // Find shifts by work area
    @Query("SELECT s FROM Shift s WHERE s.workArea = :workArea")
    List<Shift> findByWorkArea(@Param("workArea") String workArea);

    // List all shifts
    @Query("SELECT s FROM Shift s")
    List<Shift> listAll();

    // Find shifts by the actual ID
    @Query("SELECT s FROM Shift s WHERE s.shiftID = :shiftID")
    Shift findByShiftId(@Param("shiftID") int shiftID);

    // Find shifts by date
    @Query("SELECT s FROM Shift s WHERE FUNCTION('DATE', s.startTime) = :date")
    List<Shift> findAllByDate(@Param("date") LocalDate date);

    // Find shifts by date and type
    @Query("SELECT s FROM Shift s WHERE FUNCTION('DATE', s.startTime) = :date AND s.shiftType = :shiftType")
    List<Shift> findAllByDateAndType(@Param("date") LocalDate date, @Param("shiftType") String shiftType);

    @Query("SELECT s FROM Shift s WHERE FUNCTION('DATE', s.startTime) = :date AND s.shiftType = :shiftType")
    List<Shift> findAllDateAndType(@Param("date") LocalDateTime date, @Param("shiftType") String shiftType);


    @Query("SELECT s FROM Shift s WHERE s.staffShifts IS EMPTY")
    List<Shift> findShiftsWithoutStaff();

    @Query("SELECT s FROM Shift s WHERE s.startTime < :cutoffDate")
    List<Shift> findShiftsOlderThan(LocalDate cutoffDate);

    List<Shift> findAllByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

}