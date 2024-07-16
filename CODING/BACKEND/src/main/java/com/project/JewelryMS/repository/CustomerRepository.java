package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByStatus(boolean b);
    Optional<Customer> findByPhoneNumber(String n);

    @Query("SELECT c FROM Customer c WHERE c.createDate BETWEEN :startDate AND :endDate")
    List<Customer> findCustomersByCreateDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT c.staffAccount.account.accountName, COUNT(c) " +
            "FROM Customer c " +
            "WHERE c.createDate BETWEEN :startDate AND :endDate " +
            "GROUP BY c.staffAccount.staffID, c.staffAccount.account.accountName")
    List<Object[]> findCustomerSignUpsByStaff(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(c) FROM Customer c WHERE FUNCTION('DATE', c.createDate) = :date")
    long countCustomersOnDate(Date date);

    @Query("SELECT COUNT(c) FROM Customer c WHERE DATE_FORMAT(c.createDate, '%Y-%m') = :month")
    long countCustomersInMonth(@Param("month") String month);

    @Query("SELECT COUNT(c) FROM Customer c WHERE FUNCTION('YEAR', c.createDate) = :year")
    long countCustomersInYear(Year year);

    List<Customer> findAllByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(c) FROM Customer c WHERE FUNCTION('YEAR', c.createDate) = :year")
    Long findCustomerSignupsByYear(@Param("year") String year);

    @Query("SELECT c FROM Customer c WHERE " +
            "LOWER(c.email) LIKE LOWER(concat('%', :keyword, '%')) OR " +
            "LOWER(c.phoneNumber) LIKE LOWER(concat('%', :keyword, '%')) OR " +
            "LOWER(c.gender) LIKE LOWER(concat('%', :keyword, '%')) OR " +
            "LOWER(c.cusName) LIKE LOWER(concat('%', :keyword, '%'))")
    List<Customer> findCustomersByStringKeyword(@Param("keyword") String keyword);

    @Query("SELECT c FROM Customer c WHERE c.PK_CustomerID = :id")
    List<Customer> findCustomersByLongKeyword(@Param("id") Long id);

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.createDate BETWEEN :startDate AND :endDate")
    long countCustomerSignUpsByStaffInRange(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.staffAccount.account.email = :email AND c.createDate BETWEEN :startDate AND :endDate")
    long countCustomerSignUpsByStaffEmailAndDateRange(
            @Param("email") String email,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.staffAccount.account.email = :email")
    long countCustomerSignUpsByStaffEmail(@Param("email") String email);
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.staffAccount.staffID = :staffId")
    long countCustomerSignUpsByStaff(@Param("staffId") long staffId);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.cusName) LIKE LOWER(CONCAT('%', :cusName, '%'))")
    List<Customer> findByCusNameContainingIgnoreCase(@Param("cusName") String cusName);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<Customer> findByEmailContainingIgnoreCase(@Param("email") String email);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%'))")
    List<Customer> findByPhoneNumberContainingIgnoreCase(@Param("phoneNumber") String phoneNumber);

}

