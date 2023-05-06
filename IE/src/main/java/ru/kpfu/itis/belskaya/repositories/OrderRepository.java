package ru.kpfu.itis.belskaya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.belskaya.models.Order;
import ru.kpfu.itis.belskaya.models.Student;
import ru.kpfu.itis.belskaya.models.Tutor;

import java.util.List;
import java.util.Optional;

/**
 * @author Elizaveta Belskaya
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM ie_order ORDER BY id OFFSET :startIndex LIMIT :pageSize")
    Optional<List<Order>> getOrdersByPageNumber(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);


    Optional<List<Order>> getOrdersByAuthor(Student author);

    Optional<Order> findById(Long id);

    @Query("SELECT o FROM Order o " +
                    "INNER JOIN Account student ON o.author.account = student " +
                    "INNER JOIN Tutor t ON t.id = :tutorId " +
                    "INNER JOIN Account tutorAccount ON t.account = tutorAccount " +
                    "AND (tutorAccount.city = student.city AND o.online <> 'ONLINE' OR o.online <> 'OFFLINE') " +
                    "AND o.tutor IS NULL " +
                    "AND o.minRating <= t.rating " +
                    "AND (o.tutorGender = t.gender OR o.tutorGender = 'BOTH') " +
                    "AND o.subject in (select s.title from t.subjectList s) " +
            "ORDER BY o.creationDate"
    )
    Optional<List<Order>> findSuitableOrderForTutor(@Param("tutorId") Long tutorId);


    Optional<List<Order>> findOrdersByTutor(Tutor tutor);


}
