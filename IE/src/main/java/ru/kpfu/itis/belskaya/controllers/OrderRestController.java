package ru.kpfu.itis.belskaya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.kpfu.itis.belskaya.converters.OrderFormToOrderConverter;
import ru.kpfu.itis.belskaya.models.*;
import ru.kpfu.itis.belskaya.models.forms.OrderForm;
import ru.kpfu.itis.belskaya.repositories.OrderRepository;
import ru.kpfu.itis.belskaya.repositories.StudentRepository;
import ru.kpfu.itis.belskaya.repositories.SubjectRepository;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping("/api")
public class OrderRestController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Book not found"
        ));
        orderRepository.delete(order);
        return ResponseEntity.ok("Deleted");
    }

    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateOrder(@PathVariable("id") Long id,
                                              @RequestBody OrderForm orderForm,
                                              @AuthenticationPrincipal Account account) {
        Student student = studentRepository.findByAccount_Id(account.getId());
        OrderFormToOrderConverter converter = new OrderFormToOrderConverter(student);
        Order updatedOrder = (Order) converter.convert(orderForm, TypeDescriptor.valueOf(OrderForm.class), TypeDescriptor.valueOf(Order.class));
        Order orderById = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Book not found"
        ));
        updatedOrder.setId(orderById.getId());
        updatedOrder.setCreationDate(orderById.getCreationDate());
        orderRepository.save(updatedOrder);
        return ResponseEntity.ok("Updated");
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Order>> getAllOrders(@AuthenticationPrincipal Account account) {
        Student student = studentRepository.findByAccount_Id(account.getId());
        List<Order> orders = orderRepository.getOrdersByAuthor(student).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Orders not found"
        ));
        return ResponseEntity.of(Optional.of(orders));
    }

    @GetMapping("/subjects")
    @PermitAll
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return ResponseEntity.of(Optional.of(subjects));
    }



}
