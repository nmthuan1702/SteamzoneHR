package com.system.app.repositories;

import com.system.app.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("SELECT a FROM Account a WHERE a.username = :keyword OR a.email = :keyword")
    Optional<Account> getByUsernameOrEmail(@Param("keyword") String keyword);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByUsername(String username);

    @Query("SELECT a FROM Account a WHERE a.username LIKE %:search% OR a.email LIKE %:search%")
    Page<Account> searchAccounts(String search, Pageable pageable);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndIdNot(String username, Integer id);

    boolean existsByEmailAndIdNot(String email, Integer id);

}