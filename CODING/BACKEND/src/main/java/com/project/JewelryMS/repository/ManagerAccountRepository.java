package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.model.Manager.ManagerAccountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerAccountRepository extends JpaRepository<Account, Integer> {
    @Query("SELECT a FROM Account a WHERE a.role = ROLE_MANAGER")
    List<Account> findAllManagerAccounts();

    @Query("SELECT a FROM Account a WHERE a.role = ROLE_MANAGER AND a.PK_userID = :id")
    Optional<Account> findManagerAccountById(Integer id);
}
