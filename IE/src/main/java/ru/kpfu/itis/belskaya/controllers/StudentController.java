package ru.kpfu.itis.belskaya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import ru.kpfu.itis.belskaya.repositories.StudentRepository;
import ru.kpfu.itis.belskaya.repositories.SubjectRepository;
import ru.kpfu.itis.belskaya.services.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Elizaveta Belskaya
 */
@Controller
@ComponentScan({"ru.kpfu.itis.belskaya.converters", "ru.kpfu.itis.belskaya.repositories"})
@RequestMapping("/student")
public class StudentController {

    private final String STUDENT = "_____Student";

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private UserService<Student> userService;

    @Autowired
    private StudentRepository studentRepository;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfile(ModelMap map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        Student student = studentRepository.findByAccount_Id(account.getId());
        map.put("account", account);
        map.put("student", student);
        return "/views/studentProfilePage";
    }

    @RequestMapping(value = "/my_orders", method = RequestMethod.GET)
    public String getStudentOrders(ModelMap map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        Student student = studentRepository.findByAccount_Id(account.getId());
        Optional<List<Order>> orders = orderRepository.getOrdersByAuthor(student);
        if (orders.isPresent()) {
            map.put("orders", orders.get());
        } else {
            map.put("orders", null);
        }
        return "/views/studentOrdersPage";
    }

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
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Account account = (Account) authentication.getPrincipal();
                Student student = studentRepository.findByAccount_Id(account.getId());
                OrderFormToOrderConverter orderConverter = new OrderFormToOrderConverter(student);
                Order order = (Order) orderConverter.convert(orderForm, TypeDescriptor.valueOf(OrderForm.class), TypeDescriptor.valueOf(Order.class));
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
                boolean registered = userService.registerUser(user, student);
                if (registered) {
                    return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("SC#addOrderGet").build();
                } else  {
                    return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("SC#register").build() + "?status=failed";
                }
            } catch (Exception ex) {
                redirectAttributes.addFlashAttribute("message", ex.getMessage());
                return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("SC#register").build() + "?status=failed";
            }
        }

        List<City> cityList = cityRepository.findAll();
        map.put("cities", cityList);
        return "/views/studentRegistrationPage";
    }





}
