package com.zim0101.authvault.repository;

import com.zim0101.authvault.model.Account;
import com.zim0101.authvault.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);

    Optional<Account> findByUsername(String username);

    @Query("SELECT a FROM Account a JOIN a.roles r WHERE r = :role")
    List<Account> findByRole(@Param("role") Role role);

    Optional<Account> findByAuthProviderId(String authProviderId);
}