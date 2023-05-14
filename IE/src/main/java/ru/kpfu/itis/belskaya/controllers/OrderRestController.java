package ru.kpfu.itis.belskaya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.kpfu.itis.belskaya.controllers.api.OrderApi;
import ru.kpfu.itis.belskaya.converters.OrderConverter;
import ru.kpfu.itis.belskaya.models.*;
import ru.kpfu.itis.belskaya.models.dto.OrderDto;
import ru.kpfu.itis.belskaya.services.OrderService;
import ru.kpfu.itis.belskaya.services.SubjectService;
import ru.kpfu.itis.belskaya.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
public class OrderRestController implements OrderApi {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService<Student> studentService;

    @Autowired
    private UserService<Tutor> tutorService;

    @Autowired
    private SubjectService subjectService;

    public ResponseEntity<OrderDto> getOrder(Long id) {
        Order order = orderService.findOrderById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Order not found"
        ));

        OrderConverter orderConverter = new OrderConverter(null);
        OrderDto orderDto = (OrderDto) orderConverter.convert(order, TypeDescriptor.valueOf(Order.class), TypeDescriptor.valueOf(OrderDto.class));
        return ResponseEntity.ok(orderDto);
    }

    @Override
    public ResponseEntity<String> addOrder(Account account, OrderDto orderDto) {
        Student student = studentService.findByAccount_Id(account.getId());
        OrderConverter converter = new OrderConverter(student);
        Order updatedOrder = (Order) converter.convert(orderDto, TypeDescriptor.valueOf(OrderDto.class), TypeDescriptor.valueOf(Order.class));
        updatedOrder.setTutor(null);
        orderService.updateOrder(updatedOrder);
        return ResponseEntity.accepted().build();
    }

    public ResponseEntity<String> deleteOrder(Account account, Long id) {
        Order order = orderService.findOrderById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Order not found"
        ));
        if (order.getAuthor().getAccount().equals(account)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no rights to delete this order");
        }
        orderService.deleteOrder(order);
        return ResponseEntity.ok("Deleted");
    }

    public ResponseEntity<String> updateOrder(Account account, Long id, @Valid OrderDto orderDto) {
        Student student = studentService.findByAccount_Id(account.getId());
        OrderConverter converter = new OrderConverter(student);
        Order updatedOrder = (Order) converter.convert(orderDto, TypeDescriptor.valueOf(OrderDto.class), TypeDescriptor.valueOf(Order.class));
        Order orderById = orderService.findOrderById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Order not found"
        ));
        if (orderById.getAuthor().getAccount().equals(account)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no rights to delete this order");
        }
        updatedOrder.setId(orderById.getId());
        updatedOrder.setCreationDate(orderById.getCreationDate());
        updatedOrder.setCandidates(orderById.getCandidates());
        updatedOrder.setTutor(orderById.getTutor());
        orderService.updateOrder(updatedOrder);
        return ResponseEntity.ok("Updated");
    }

    public ResponseEntity<String> addTutorToOrder(Long id, Account account) {
        Tutor tutor = tutorService.findByAccount_Id(account.getId());
        Order order = orderService.findOrderById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Order not found"
        ));
        order.getCandidates().add(tutor);
        orderService.updateOrder(order);
        return ResponseEntity.ok("Updated");
    }

    public ResponseEntity<List<OrderDto>> getAllOrders(Account account) {
        Student student = studentService.findByAccount_Id(account.getId());
        OrderConverter orderConverter = new OrderConverter(student);
        List<Order> orders = orderService.getOrdersByAuthor(student).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Orders not found"
        ));
        List<OrderDto> orderDtoList = orders.stream()
                .map(o -> (OrderDto) orderConverter.convert(o, TypeDescriptor.valueOf(Order.class), TypeDescriptor.valueOf(OrderDto.class))).collect(Collectors.toList());
        return ResponseEntity.ok(orderDtoList);
    }

    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.findAllSubjects();
        return ResponseEntity.ok(subjects);
    }


}
