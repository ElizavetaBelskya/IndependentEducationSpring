package ru.kpfu.itis.belskaya.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.kpfu.itis.belskaya.converters.OrderFormToOrderConverter;
import ru.kpfu.itis.belskaya.models.Order;
import ru.kpfu.itis.belskaya.models.Student;
import ru.kpfu.itis.belskaya.models.Tutor;
import ru.kpfu.itis.belskaya.models.forms.OrderForm;
import ru.kpfu.itis.belskaya.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final int PAGE_SIZE = 3;

    @Autowired
    private OrderRepository orderRepository;

    public Optional<List<Order>> getOrdersByAuthor(Student student) {
        return orderRepository.getOrdersByAuthor(student);
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
        orderRepository.delete(order);
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

    public void cancelTutor(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            order.get().setTutor(null);
            orderRepository.save(order.get());
        }
    }
}
