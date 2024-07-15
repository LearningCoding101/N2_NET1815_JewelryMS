package com.project.JewelryMS.repository;


import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.enumClass.RoleEnum;
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

    @Query("SELECT a FROM Account a")
    List<Account> findAllAccounts();

    //Handle Duplicate Exception
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.aUsername = :aUsername")
    boolean existsByAUsername(@Param("aUsername") String aUsername);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.aUsername = :aUsername AND a.PK_userID <> :userId")
    boolean existsByAUsernameAndPkUserIDNot(@Param("aUsername") String aUsername, @Param("userId") int userId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.email = :email AND a.PK_userID <> :userId")
    boolean existsByEmailAndPkUserIDNot(@Param("email") String email, @Param("userId") int userId);
}
