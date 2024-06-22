package com.project.JewelryMS.repository;


import com.project.JewelryMS.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByStatus(boolean b);
    Optional<Customer> findByPhoneNumber(String n);

    @Query("SELECT c FROM Customer c WHERE c.createDate BETWEEN :startDate AND :endDate")
    List<Customer> findCustomersByCreateDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT c.staffAccount.account.accountName, COUNT(c) FROM Customer c WHERE c.createDate BETWEEN :startDate AND :endDate GROUP BY c.staffAccount.account.accountName")
    List<Object[]> findCustomerSignUpsByStaff(LocalDateTime startDate, LocalDateTime endDate);
}