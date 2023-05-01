package ru.kpfu.itis.belskaya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.belskaya.models.*;
import ru.kpfu.itis.belskaya.models.forms.StudentForm;
import ru.kpfu.itis.belskaya.models.forms.TutorForm;
import ru.kpfu.itis.belskaya.repositories.CityRepository;
import ru.kpfu.itis.belskaya.repositories.OrderRepository;
import ru.kpfu.itis.belskaya.repositories.SubjectRepository;
import ru.kpfu.itis.belskaya.services.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Elizaveta Belskaya
 */
@Controller
@RequestMapping("/tutor")
public class TutorController {

    private final int PAGE_SIZE = 3;

    private final String TUTOR = "_____Tutor";

    @Autowired
    private UserService<Tutor> userService;

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

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPost(RedirectAttributes redirectAttributes,
                               @ModelAttribute("userForm") @Valid TutorForm tutorForm,
                               BindingResult result,
                               ModelMap map) {
        if (!result.hasErrors()) {
            Account user = Account.builder()
                    .emailAndRole(tutorForm.getEmail() + TUTOR)
                    .name(tutorForm.getName())
                    .passwordHash(tutorForm.getPassword())
                    .role(Account.Role.TUTOR)
                    .state(Account.State.ACTIVE)
                    .city(tutorForm.getCity())
                    .build();

            Tutor tutor = Tutor.builder()
                    .email(tutorForm.getEmail())
                    .phone(tutorForm.getPhone())
                    .gender(tutorForm.isGender())
                    .subjectList(tutorForm.getSubjects())
                    .isWorkingOnline(tutorForm.getIsWorkingOnline())
                    .account(user).build();
            try {
                userService.registerUser(user, tutor);
                redirectAttributes.addFlashAttribute("message", "Successfully registered!");
            } catch (Exception ex) {
                redirectAttributes.addFlashAttribute("message", ex.getMessage());
            }
        }

        List<City> cityList = cityRepository.findAll();
        map.put("cities", cityList);
        map.put("subjects", subjectRepository.findAll());
        return "/views/tutorRegistrationPage";
    }



}
