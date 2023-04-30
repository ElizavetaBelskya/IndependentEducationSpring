package ru.kpfu.itis.belskaya.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.belskaya.models.Student;

import java.util.Optional;


/**
 * @author Elizaveta Belskaya
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findAccountByEmailOrPhone(String email, String phone);

    Student findByAccount_Id(Long accountId);

}
