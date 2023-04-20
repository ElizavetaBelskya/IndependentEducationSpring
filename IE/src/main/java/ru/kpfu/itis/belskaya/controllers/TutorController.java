package ru.kpfu.itis.belskaya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.belskaya.models.Order;
import ru.kpfu.itis.belskaya.models.forms.TutorForm;
import ru.kpfu.itis.belskaya.repositories.CityRepository;
import ru.kpfu.itis.belskaya.repositories.OrderRepository;
import ru.kpfu.itis.belskaya.repositories.SubjectRepository;

import java.util.List;

/**
 * @author Elizaveta Belskaya
 */
@Controller
@RequestMapping("/tutor")
public class TutorController {

    private int PAGE_SIZE = 3;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CityRepository cityRepository;

    @RequestMapping(value = "/orders", method = RequestMethod.GET, params = {"page"})
    public String orders(@RequestParam Integer page, ModelMap map) {
        List<Order> orders = orderRepository.getOrdersByPageNumber((page-1)*PAGE_SIZE, PAGE_SIZE);
        System.out.println(orders);
        map.put("orders", orders);
        map.put("page", page);
        map.put("countOfPages", orderRepository.count()/PAGE_SIZE + orderRepository.count()%PAGE_SIZE);
        return "/views/newOrdersPage";
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String orders(ModelMap map) {
        int page = 1;
        List<Order> orders = orderRepository.getOrdersByPageNumber(page, PAGE_SIZE);
        map.put("orders", orders);
        map.put("page", page);
        map.put("countOfPages", orderRepository.count()/PAGE_SIZE + orderRepository.count()%PAGE_SIZE);
        return "/views/newOrdersPage";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(ModelMap map) {
        map.put("userForm", new TutorForm());
        map.put("cities", cityRepository.findAll());
        map.put("subjects", subjectRepository.findAll());
        return "/views/tutorRegistrationPage";
    }


}
