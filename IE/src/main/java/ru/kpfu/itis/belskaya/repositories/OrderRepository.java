package ru.kpfu.itis.belskaya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.belskaya.models.*;
import ru.kpfu.itis.belskaya.models.Order;

import java.util.List;
import java.util.Optional;

/**
 * @author Elizaveta Belskaya
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM ie_order ORDER BY id OFFSET :startIndex LIMIT :pageSize")
    Optional<List<Order>> getOrdersByPageNumber(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);


    @Query("SELECT o from Order o where o.author = :author and o.state = 'ACTUAL'")
    Optional<List<Order>> findAllByAuthor(@Param("author") Student author);

    Optional<Order> findById(Long id);

    @Query("SELECT o FROM Order o " +
                    "INNER JOIN Account student on o.author.account = student " +
                    "INNER JOIN Tutor t ON t.id = :tutorId " +
                    "INNER JOIN Account tutorAccount ON t.account = tutorAccount " +
                    "AND (tutorAccount.city = student.city AND o.online <> 'ONLINE' OR o.online <> 'OFFLINE') " +
                    "AND o.tutor IS NULL AND NOT t.id IN (select candidates.id from o.candidates candidates) " +
                    "AND o.minRating <= t.rating " +
                    "AND (o.tutorGender = t.gender OR o.tutorGender = 'BOTH') " +
                    "AND o.subject in (select s.title from t.subjectList s) AND o.state = 'ACTUAL' " +
            "ORDER BY o.creationDate"
    )
    Optional<List<Order>> findSuitableOrderForTutor(@Param("tutorId") Long tutorId);


//    с подзапросом
    @Query(nativeQuery = true, value = "WITH t AS (SELECT * FROM ie_tutor WHERE id = :tutorId LIMIT 1)," +
            " subject_ids AS (SELECT subject_id FROM tutor_subject WHERE tutor_id = :tutorId) " +
            "SELECT * FROM ie_order WHERE (tutor_id IS NULL AND min_rating <= (SELECT rating FROM t) " +
            "AND subject IN (SELECT title FROM subject WHERE id IN (SELECT * FROM subject_ids)) " +
            "AND ((SELECT gender FROM t) = tutor_gender OR tutor_gender = 'BOTH') " +
            "AND (((SELECT ia.city_id FROM ie_account ia " +
            "WHERE ia.id = (SELECT account_id FROM t)) = (SELECT ia.city_id FROM ie_student INNER JOIN" +
            " ie_account ia ON ia.id = ie_student.account_id AND ie_student.id = ie_order.author_id) " +
            "AND ie_order.online = 'OFFLINE') OR ((SELECT is_working_online FROM t) AND ie_order.online <> 'OFFLINE')) " +
            "AND id NOT IN (SELECT order_id FROM order_tutor WHERE tutor_id = :tutorId) AND state = 'ACTUAL') ORDER BY created_at")
    Optional<List<Order>> findSuitableOrderForTutorAlternative(@Param("tutorId") Long tutorId);



    Optional<List<Order>> findOrdersByTutor(Tutor tutor);


    @Query("SELECT o FROM Order o WHERE o.tutor is null and o.author.id = :studentId and o.state = 'ACTUAL'")
    List<Order> findOrdersByStudentWithoutTutor(@Param("studentId") Long studentId);


    @Query("SELECT DISTINCT t FROM Tutor t INNER JOIN Order o ON o.tutor = t WHERE o.author.id = :studentId")
    List<Tutor> findTutorsOfStudent(@Param("studentId") Long studentId);


    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.tutor = null WHERE o.author.id = :studentId AND o.tutor.id = :rejectId")
    void rejectTutorFromStudentOrders(@Param("studentId") Long studentId, @Param("rejectId") Long rejectId);



}
