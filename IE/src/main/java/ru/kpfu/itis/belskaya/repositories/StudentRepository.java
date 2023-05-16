package ru.kpfu.itis.belskaya.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.belskaya.models.Student;
import ru.kpfu.itis.belskaya.models.Tutor;

import java.util.List;
import java.util.Optional;


/**
 * @author Elizaveta Belskaya
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findAccountByEmailOrPhone(String email, String phone);

    Student findByAccount_Id(Long accountId);

    @Query("SELECT count(s) from Student s inner join Order o on o.author = s " +
            "and o.tutor = :tutor and o.subject = :subject")
    int findStudentsCountBySubject(@Param("tutor") Tutor tutor, @Param("subject") String subject);

    @Query("SELECT count(s) from Student s inner join Order o on o.author = s and o.tutor = :tutor")
    int getStudentsCountByTutor(@Param("tutor") Tutor tutor);

}
