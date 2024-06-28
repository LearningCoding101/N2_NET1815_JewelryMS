package com.project.JewelryMS.repository;


import com.project.JewelryMS.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByStatus(boolean b);
    Optional<Customer> findByPhoneNumber(String n);

    @Query("SELECT c FROM Customer c WHERE c.createDate BETWEEN :startDate AND :endDate")
    List<Customer> findCustomersByCreateDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT c.staffAccount.staffID, c.staffAccount.account.accountName, COUNT(c) " +
            "FROM Customer c " +
            "WHERE c.createDate BETWEEN :startDate AND :endDate " +
            "GROUP BY c.staffAccount.staffID, c.staffAccount.account.accountName")
    List<Object[]> findCustomerSignUpsByStaff(LocalDateTime startDate, LocalDateTime endDate);


    @Query("SELECT COUNT(c) FROM Customer c WHERE FUNCTION('DATE', c.createDate) = :date")
    long countCustomersOnDate(Date date);
//DATE_FORMAT(c.createDate, '%Y-%m') = :month
//  FUNCTION('YEAR_MONTH', c.createDate) = FUNCTION('YEAR_MONTH', :month)
    @Query("SELECT COUNT(c) FROM Customer c WHERE DATE_FORMAT(c.createDate, '%Y-%m') = :month")
    long countCustomersInMonth(@Param("month") String month);

    @Query("SELECT COUNT(c) FROM Customer c WHERE FUNCTION('YEAR', c.createDate) = :year")
    long countCustomersInYear(Year year);

    List<Customer> findAllByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}