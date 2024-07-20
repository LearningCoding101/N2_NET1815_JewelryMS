package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.entity.WorkArea;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StaffAccountRepository extends JpaRepository<StaffAccount, Integer> {

   @Query("SELECT sa FROM StaffAccount sa JOIN sa.account a WHERE a.role = 'ROLE_STAFF'")
        List<StaffAccount> findAllStaffAccountsByRoleStaff();

   @Query("SELECT sa FROM StaffAccount sa JOIN sa.account a WHERE a.role = 'ROLE_STAFF' AND a.status = 1 AND sa.staffID = :id")
        Optional<StaffAccount> findIDStaffAccount(@Param("id") Integer id);
    @Query("SELECT sa FROM StaffAccount sa JOIN sa.staffShifts ss WHERE ss.shift IN :shifts")
    List<StaffAccount> findAllByShifts(@Param("shifts") List<Shift> shifts);
    // Find all staff accounts assigned to a specific shift
    @Query("SELECT sa FROM StaffAccount sa JOIN sa.staffShifts ss WHERE ss.shift = :shift")
    List<StaffAccount> findAllByShift(@Param("shift") Shift shift);

    // Find all staff accounts assigned to shifts within a specific date range
    @Query("SELECT DISTINCT sa FROM StaffAccount sa LEFT JOIN FETCH sa.staffShifts ss LEFT JOIN FETCH ss.shift s WHERE s.startTime BETWEEN :startDate AND :endDate")
    List<StaffAccount> findAllStaffAccountsByShifts(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT s FROM StaffAccount s WHERE s.staffShifts IS EMPTY")
    List<StaffAccount> findStaffWithoutShift();

    @Query("SELECT sa FROM StaffAccount sa WHERE sa.account.email = :email")
    StaffAccount findByEmail(@Param("email") String email);

    @Query("SELECT sa.account.email FROM StaffAccount sa WHERE sa.staffID = :staffId")
    String findEmailByStaffId(@Param("staffId") long staffId);

    @Query("SELECT DISTINCT s FROM StaffAccount s LEFT JOIN FETCH s.workArea WHERE s.staffID IN :staffIds")
    List<StaffAccount> findAllByIdWithWorkAreaEagerly(Set<Integer> staffIds);

    @Query("SELECT sa FROM StaffAccount sa WHERE sa.account.aUsername =:aUsername")
    Optional<StaffAccount> findByUsername(@Param("aUsername") String username);


    @Query("SELECT sa FROM StaffAccount sa WHERE sa.account.accountName LIKE %:accountName%")
    List<StaffAccount> findByAccountNameContaining(@Param("accountName") String accountName);


}