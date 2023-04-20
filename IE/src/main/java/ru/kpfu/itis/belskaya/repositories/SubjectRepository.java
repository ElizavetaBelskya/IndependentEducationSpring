package ru.kpfu.itis.belskaya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.belskaya.models.Subject;

import java.util.List;

/**
 * @author Elizaveta Belskaya
 */
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query("SELECT s.title FROM Subject s")
    List<String> findAllTitles();

}
