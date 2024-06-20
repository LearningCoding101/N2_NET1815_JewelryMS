package com.project.JewelryMS.repository;


import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthenticationRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.aUsername = ?1")
    Account findAccountByUsername(String Username);
    @Query("SELECT a FROM Account a WHERE a.email = ?1")
    Account findAccountByemail(String email);
    List<Account> findByRole(RoleEnum role);
    @Query("SELECT a FROM Account a WHERE a.PK_userID = ?1")
    Account findAccountById(Long id);

    @Query("SELECT a FROM Account a WHERE a.aPassword = :password AND a.PK_userID = :id")
    Account checkAccountByPassword(@Param("password") String password, @Param("id") Long id);

}
