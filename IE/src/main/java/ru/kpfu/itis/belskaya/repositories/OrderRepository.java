package ru.kpfu.itis.belskaya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.belskaya.models.*;
import ru.kpfu.itis.belskaya.models.Order;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

/**
 * @author Elizaveta Belskaya
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM ie_order ORDER BY id OFFSET :startIndex LIMIT :pageSize")
    Optional<List<Order>> getOrdersByPageNumber(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);


    Optional<List<Order>> findAllByAuthor(Student author);

    Optional<Order> findById(Long id);

    @Query("SELECT o FROM Order o " +
                    "INNER JOIN Account student ON o.author.account = student " +
                    "INNER JOIN Tutor t ON t.id = :tutorId " +
                    "INNER JOIN Account tutorAccount ON t.account = tutorAccount " +
                    "AND (tutorAccount.city = student.city AND o.online <> 'ONLINE' OR o.online <> 'OFFLINE') " +
                    "AND o.tutor IS NULL AND NOT t.id IN (select candidates.id from o.candidates candidates) " +
                    "AND o.minRating <= t.rating " +
                    "AND (o.tutorGender = t.gender OR o.tutorGender = 'BOTH') " +
                    "AND o.subject in (select s.title from t.subjectList s) " +
            "ORDER BY o.creationDate"
    )
    Optional<List<Order>> findSuitableOrderForTutor(@Param("tutorId") Long tutorId);





//    default public Optional<List<Order>> findSuitableOrderForTutorCB(Long tutorId) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Order> query = cb.createQuery(Order.class);
//
//        Root<Order> order = query.from(Order.class);
//        Join<Order, Account> author = order.join("author", JoinType.INNER);
//        Join<Account, Student> student = cb.treat(author, Student.class);
//        Join<Order, Tutor> tutor = order.join("tutor", JoinType.LEFT);
//        Join<Tutor, Account> tutorAccount = tutor.join("account", JoinType.INNER);
//        Join<Tutor, Subject> subjectList = tutor.join("subjectList", JoinType.INNER);
//
//        Subquery<Long> subquery = query.subquery(Long.class);
//        Root<Order> candidates = subquery.from(Order.class);
//        subquery.select(candidates.get("id")).where(cb.equal(candidates.get("tutor"), tutor));
//
//        Predicate predicate = cb.and(
//                cb.isNull(order.get("tutor")),
//                cb.not(tutor.get("id").in(subquery)),
//                cb.le(order.get("minRating"), tutor.get("rating")),
//                cb.or(
//                        cb.equal(order.get("tutorGender"), tutor.get("gender")),
//                        cb.equal(order.get("tutorGender"), Gender.BOTH)
//                ),
//                order.get("subject").in(subjectList.select(subjectList.get("title"))),
//                cb.and(
//                        cb.equal(student.get("city"), tutorAccount.get("city")),
//                        cb.or(
//                                cb.notEqual(order.get("online"), Order.Format.ONLINE),
//                                cb.notEqual(order.get("online"), Order.Format.OFFLINE)
//                        )
//                )
//        );
//
//        query.select(order).distinct(true).where(predicate).orderBy(cb.asc(order.get("creationDate")));
//
//        TypedQuery<Order> typedQuery = entityManager.createQuery(query);
//        typedQuery.setParameter("tutorId", tutorId);
//
//        List<Order> resultList = typedQuery.getResultList();
//        return Optional.of(resultList);
//    }


    Optional<List<Order>> findOrdersByTutor(Tutor tutor);


    @Query("SELECT o FROM Order o WHERE o.tutor is null and o.author.id = :studentId")
    List<Order> findOrdersByStudentWithoutTutor(@Param("studentId") Long studentId);


    @Query("SELECT DISTINCT t FROM Tutor t INNER JOIN Order o ON o.tutor = t WHERE o.author.id = :studentId")
    List<Tutor> findTutorsOfStudent(@Param("studentId") Long studentId);


    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.tutor = null WHERE o.author.id = :studentId AND o.tutor.id = :rejectId")
    void rejectTutorFromStudentOrders(@Param("studentId") Long studentId, @Param("rejectId") Long rejectId);


}
