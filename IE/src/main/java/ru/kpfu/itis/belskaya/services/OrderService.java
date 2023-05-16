package ru.kpfu.itis.belskaya.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.belskaya.models.Order;
import ru.kpfu.itis.belskaya.models.Student;
import ru.kpfu.itis.belskaya.models.Tutor;
import ru.kpfu.itis.belskaya.repositories.OrderRepository;
import ru.kpfu.itis.belskaya.repositories.TutorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final int PAGE_SIZE = 3;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TutorRepository tutorRepository;

    public Optional<List<Order>> getOrdersByAuthor(Student student) {
        return orderRepository.findAllByAuthor(student);
    }

    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    public Optional<List<Order>> getOrdersByPage(int page) {
        return orderRepository.getOrdersByPageNumber((page-1)*PAGE_SIZE, PAGE_SIZE);
    }

    public long getCountOfPages() {
        return orderRepository.count()/PAGE_SIZE + orderRepository.count()%PAGE_SIZE;
    }

    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public void deleteOrder(Order order) {
        order.setState(Order.State.DELETED);
        orderRepository.save(order);
    }

    public void updateOrder(Order updatedOrder) {
        orderRepository.save(updatedOrder);
    }


    public Optional<List<Order>> getSuitableOrders(Long tutorId) {
        return orderRepository.findSuitableOrderForTutor(tutorId);
    }

    public Optional<List<Order>> getOrdersByTutor(Tutor tutor) {
        return orderRepository.findOrdersByTutor(tutor);
    }

    public void cancelTutor(Long orderId, Tutor tutor) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            order.get().setTutor(null);
            order.get().getCandidates().remove(tutor);
            orderRepository.save(order.get());
        }
    }

    public List<Order> getUncompletedOrdersByStudent(Student student) {
        return orderRepository.findOrdersByStudentWithoutTutor(student.getId());
    }


    public List<Tutor> getTutorsOfStudent(Student student) {
        return orderRepository.findTutorsOfStudent(student.getId());
    }

    public void setTutorToOrder(Long orderId, Long tutorId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Optional<Tutor> tutor = tutorRepository.findById(tutorId);
            if (tutor.isPresent()) {
                order.get().setTutor(tutor.get());
                orderRepository.save(order.get());
            }
        }
    }

    public void rejectTutorFromStudentOrders(Student student, Long rejectId) {
        orderRepository.rejectTutorFromStudentOrders(student.getId(), rejectId);
    }

}
