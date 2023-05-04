package ru.kpfu.itis.belskaya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.belskaya.models.Student;
import ru.kpfu.itis.belskaya.models.Tutor;

import java.util.Optional;

/**
 * @author Elizaveta Belskaya
 */

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    Optional<Tutor> findAccountByEmailOrPhone(String email, String phone);

    Tutor findByAccount_Id(Long accountId);


}
