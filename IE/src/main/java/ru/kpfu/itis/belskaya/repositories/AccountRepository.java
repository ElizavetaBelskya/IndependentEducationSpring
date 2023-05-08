package ru.kpfu.itis.belskaya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.belskaya.models.Account;

import java.util.Optional;

/**
 * @author Elizaveta Belskaya
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Optional<Account> findAccountById(@Param("id") Long id);

    Optional<Account> findByEmailAndAndRole(String email, Account.Role role);

}