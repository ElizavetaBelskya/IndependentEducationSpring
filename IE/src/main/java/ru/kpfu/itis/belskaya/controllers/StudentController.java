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
import ru.kpfu.itis.belskaya.converters.StudentFormToAccountAndStudentConverter;
import ru.kpfu.itis.belskaya.models.Account;
import ru.kpfu.itis.belskaya.models.City;
import ru.kpfu.itis.belskaya.models.Order;
import ru.kpfu.itis.belskaya.models.Student;
import ru.kpfu.itis.belskaya.models.forms.OrderForm;
import ru.kpfu.itis.belskaya.models.forms.StudentForm;
import ru.kpfu.itis.belskaya.services.*;
import ru.kpfu.itis.belskaya.validators.EmailAndPhoneValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Elizaveta Belskaya
 */
@Controller
@ComponentScan({"ru.kpfu.itis.belskaya.converters",
        "ru.kpfu.itis.belskaya.repositories",
        "ru.kpfu.itis.belskaya.validators",
        "ru.kpfu.itis.belskaya.services"})
@RequestMapping("/student")
public class StudentController {


    @Autowired
    private EmailAndPhoneValidator emailAndPhoneValidator;

    private final String STUDENT = "_____Student";

    @Autowired
    private OrderService orderService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CityService cityService;

    @Autowired
    private UserService<Student> userServiceStudent;

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfile(ModelMap map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        Student student = userServiceStudent.findByAccount_Id(account.getId());
        map.put("account", account);
        map.put("student", student);
        return "/views/studentProfilePage";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String deleteProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        accountService.deleteAccount(account);
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("MC#login").build();
    }


    @RequestMapping(value = "/my_orders", method = RequestMethod.GET)
    public String getStudentOrders(ModelMap map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        Student student = userServiceStudent.findByAccount_Id(account.getId());
        Optional<List<Order>> orders = orderService.getOrdersByAuthor(student);
        if (orders.isPresent()) {
            map.put("orders", orders.get());
        } else {
            map.put("orders", null);
        }
        return "/views/studentOrdersPage";
    }

    @RequestMapping(value = "/new_order", method = RequestMethod.GET)
    public String addOrderGet(ModelMap map) {
        map.put("subjects", subjectService.findAllTitles());
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
        map.put("subjects", subjectService.findAllTitles());
        if (result.hasErrors()) {
            return "/views/orderCreationPage";
        } else {
            if (orderForm != null) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Account account = (Account) authentication.getPrincipal();
                Student student = userServiceStudent.findByAccount_Id(account.getId());
                OrderFormToOrderConverter orderConverter = new OrderFormToOrderConverter(student);
                Order order = (Order) orderConverter.convert(orderForm, TypeDescriptor.valueOf(OrderForm.class), TypeDescriptor.valueOf(Order.class));
                orderService.createOrder(order);
                redirectAttributes.addFlashAttribute("answer", "Successfully created");
            }
            return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("SC#addOrderGet").build();
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(ModelMap map) {
        map.put("cities", cityService.findAll());
        map.put("userForm", new StudentForm());
        return "/views/studentRegistrationPage";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPost(RedirectAttributes redirectAttributes,
                               @ModelAttribute("userForm") @Valid StudentForm studentForm,
                               BindingResult result,
                               ModelMap map) {
        if (!result.hasErrors()) {
            if (!emailAndPhoneValidator.validateEmail(studentForm.getEmail())) {
                redirectAttributes.addFlashAttribute("message", "Your email is not real");
                return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("SC#register").build() + "?status=failed";
            } else if (!emailAndPhoneValidator.validatePhone(studentForm.getPhone(), studentForm.getCity().getCountryCode())) {
                redirectAttributes.addFlashAttribute("message", "Your phone is not real or you selected wrong country");
                return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("SC#register").build() + "?status=failed";
            }
            StudentFormToAccountAndStudentConverter converter = new StudentFormToAccountAndStudentConverter();
            Account account = (Account) converter.convert(studentForm, TypeDescriptor.valueOf(StudentForm.class), TypeDescriptor.valueOf(Account.class));
            Student student = (Student) converter.convert(studentForm, TypeDescriptor.valueOf(StudentForm.class), TypeDescriptor.valueOf(Student.class));
            try {
                boolean registered = accountService.registerUser(account, student);
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

        List<City> cityList = cityService.findAll();
        map.put("cities", cityList);
        return "/views/studentRegistrationPage";
    }





}
