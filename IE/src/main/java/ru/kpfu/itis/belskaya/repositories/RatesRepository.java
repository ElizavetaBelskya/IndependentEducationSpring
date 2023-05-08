package ru.kpfu.itis.belskaya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.belskaya.models.Rate;
import ru.kpfu.itis.belskaya.models.Student;
import ru.kpfu.itis.belskaya.models.Tutor;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatesRepository extends JpaRepository<Rate, Long> {

    @Query("SELECT AVG(r.rating) FROM Rate r WHERE r.tutor.id = :tutorId")
    Float changeRating(@Param("tutorId") Long tutorId);

    @Query("SELECT r from Rate r WHERE r.tutor = :tutor and r.student = :student")
    Optional<Rate> findByTutorAndStudent(@Param("tutor") Tutor tutor, @Param("student") Student student);

    Optional<List<Rate>> findAllByTutor(Tutor tutor);

}
