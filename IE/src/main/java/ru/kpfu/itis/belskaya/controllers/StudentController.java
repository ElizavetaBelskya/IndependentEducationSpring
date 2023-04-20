package ru.kpfu.itis.belskaya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.belskaya.converters.OrderFormToOrderConverter;
import ru.kpfu.itis.belskaya.models.Account;
import ru.kpfu.itis.belskaya.models.City;
import ru.kpfu.itis.belskaya.models.Order;
import ru.kpfu.itis.belskaya.models.Student;
import ru.kpfu.itis.belskaya.models.forms.OrderForm;
import ru.kpfu.itis.belskaya.models.forms.StudentForm;
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
@ComponentScan({"ru.kpfu.itis.belskaya.converters", "ru.kpfu.itis.belskaya.repositories"})
@RequestMapping("/student")
public class StudentController {

    private String STUDENT = "_____Student";

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private OrderFormToOrderConverter orderFormToOrderConverter;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private UserService<Student> userService;

    @RequestMapping(value = "/new_order", method = RequestMethod.GET)
    public String addOrderGet(ModelMap map) {
        map.put("subjects", subjectRepository.findAllTitles());
        map.put("order", new OrderForm());
        return "/views/orderCreationPage";
    }

    @RequestMapping(value = "/new_order", method = RequestMethod.POST)
    public String addOrder(
            RedirectAttributes redirectAttributes,
            @ModelAttribute("order") @Valid OrderForm orderForm,
            BindingResult result,
            ModelMap map
    ) {
        map.put("subjects", subjectRepository.findAllTitles());
        if (result.hasErrors()) {
            return "/views/orderCreationPage";
        } else {
            if (orderForm != null) {
                Order order = (Order) orderFormToOrderConverter.convert(orderForm, TypeDescriptor.valueOf(OrderForm.class), TypeDescriptor.valueOf(Order.class));
                orderRepository.save(order);
                redirectAttributes.addFlashAttribute("answer", "Successfully created");
            }
            return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("SC#addOrderGet").build();
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(ModelMap map) {
        map.put("cities", cityRepository.findAll());
        map.put("userForm", new StudentForm());
        return "/views/studentRegistrationPage";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPost(RedirectAttributes redirectAttributes,
                               @ModelAttribute("userForm") @Valid StudentForm studentForm,
                               BindingResult result,
                               ModelMap map) {
        if (!result.hasErrors()) {
            Account user = Account.builder()
                    .emailAndRole(studentForm.getEmail() + STUDENT)
                    .name(studentForm.getName())
                    .passwordHash(studentForm.getPassword())
                    .role(Account.Role.STUDENT)
                    .state(Account.State.ACTIVE)
                    .city(studentForm.getCity())
                    .build();

            Student student = Student.builder()
                    .email(studentForm.getEmail())
                    .phone(studentForm.getPhone())
                    .account(user).build();
            try {
                userService.registerUser(user, student);
                redirectAttributes.addFlashAttribute("message", "Successfully registered!");
            } catch (Exception ex) {
                redirectAttributes.addFlashAttribute("message", ex.getMessage());
            }
        }

        List<City> cityList = cityRepository.findAll();
        map.put("cities", cityList);
        return "/views/studentRegistrationPage";
    }


}
