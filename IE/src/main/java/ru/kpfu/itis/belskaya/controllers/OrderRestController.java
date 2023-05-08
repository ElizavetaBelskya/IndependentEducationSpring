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
import ru.kpfu.itis.belskaya.services.OrderService;
import ru.kpfu.itis.belskaya.services.SubjectService;
import ru.kpfu.itis.belskaya.services.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping("/api")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService<Student> studentService;

    @Autowired
    private UserService<Tutor> tutorService;

    @Autowired
    private SubjectService subjectService;

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id) {
        Order order = orderService.findOrderById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Book not found"
        ));
        orderService.deleteOrder(order);
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateOrder(@PathVariable("id") Long id,
                                              @RequestBody OrderForm orderForm,
                                              @AuthenticationPrincipal Account account) {
        Student student = studentService.findByAccount_Id(account.getId());
        OrderFormToOrderConverter converter = new OrderFormToOrderConverter(student);
        Order updatedOrder = (Order) converter.convert(orderForm, TypeDescriptor.valueOf(OrderForm.class), TypeDescriptor.valueOf(Order.class));
        Order orderById = orderService.findOrderById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Order not found"
        ));
        updatedOrder.setId(orderById.getId());
        updatedOrder.setCreationDate(orderById.getCreationDate());
        orderService.updateOrder(updatedOrder);
        return ResponseEntity.ok("Updated");
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('TUTOR')")
    public ResponseEntity<String> addTutorToOrder(@PathVariable("id") Long id,
                                              @AuthenticationPrincipal Account account) {
        Tutor tutor = tutorService.findByAccount_Id(account.getId());
        Order order = orderService.findOrderById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Order not found"
        ));
        order.getCandidates().add(tutor);
        orderService.updateOrder(order);
        return ResponseEntity.ok("Updated");
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Order>> getAllOrders(@AuthenticationPrincipal Account account) {
        Student student = studentService.findByAccount_Id(account.getId());
        List<Order> orders = orderService.getOrdersByAuthor(student).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Orders not found"
        ));
        return ResponseEntity.of(Optional.of(orders));
    }

    @GetMapping("/subjects")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.findAllSubjects();
        return ResponseEntity.of(Optional.of(subjects));
    }


}
