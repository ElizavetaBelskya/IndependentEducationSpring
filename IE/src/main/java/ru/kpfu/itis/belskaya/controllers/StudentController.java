package ru.kpfu.itis.belskaya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.belskaya.converters.OrderConverter;
import ru.kpfu.itis.belskaya.converters.StudentFormToAccountAndStudentConverter;
import ru.kpfu.itis.belskaya.models.*;
import ru.kpfu.itis.belskaya.models.forms.OrderForm;
import ru.kpfu.itis.belskaya.models.forms.RateDto;
import ru.kpfu.itis.belskaya.models.forms.StudentForm;
import ru.kpfu.itis.belskaya.services.*;
import ru.kpfu.itis.belskaya.validators.EmailAndPhoneApiValidator;

import javax.validation.Valid;
import java.util.ArrayList;
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
    private EmailAndPhoneApiValidator emailAndPhoneApiValidator;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CityService cityService;

    @Autowired
    private UserService<Student> userServiceStudent;

    @Autowired
    private AccountService accountService;

    @PreAuthorize("hasAuthority('STUDENT')")
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfile(ModelMap map, @AuthenticationPrincipal Account account) {
        Student student = userServiceStudent.findByAccountId(account.getId());
        map.put("account", account);
        map.put("student", student);
        return "/views/studentProfilePage";
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @RequestMapping(value = "/my_tutors", method = RequestMethod.GET)
    public String getTutorsAndCandidates(ModelMap map, @AuthenticationPrincipal Account account) {
        Student student = userServiceStudent.findByAccountId(account.getId());
        List<Order> uncompletedOrders = orderService.getUncompletedOrdersByStudent(student);
        List<List<Tutor>> candidatesLists = new ArrayList<>();
        for (Order order : uncompletedOrders) {
            candidatesLists.add(order.getCandidates());
        }
        List<Tutor> approvedTutors = orderService.getTutorsOfStudent(student);
        map.put("uncompletedOrders", uncompletedOrders);
        map.put("candidatesLists", candidatesLists);
        map.put("approvedTutors", approvedTutors);
        return "/views/tutorsAndCandidatesListPage";
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @RequestMapping(value = "/my_tutors", method = RequestMethod.POST, params = "action=choose")
    public String chooseTutor(@RequestParam("chooseOrderId") Long chooseOrderId,
                              @RequestParam("chooseTutorId") Long chooseTutorId) {
        orderService.setTutorToOrder(chooseOrderId, chooseTutorId);
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("SC#getTutorsAndCandidates").build();
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @RequestMapping(value = "/my_tutors", method = RequestMethod.POST, params = "action=reject")
    public String rejectTutor(@RequestParam("rejectId") Long rejectId, @AuthenticationPrincipal Account account) {
        Student student = userServiceStudent.findByAccountId(account.getId());
        orderService.rejectTutorFromStudentOrders(student, rejectId);
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("SC#getTutorsAndCandidates").build();
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @RequestMapping(value = "/my_tutors", method = RequestMethod.POST, params = "action=rated")
    public String rateTutor(@RequestParam("idRatedTutor") Long id, @RequestParam("starCount") int starCount,
                            @RequestParam("comment") String comment,
                            @AuthenticationPrincipal Account account) {
        Student student = userServiceStudent.findByAccountId(account.getId());
        RateDto rateDto = RateDto.builder().tutorId(id).student(student).rating(starCount).comment(comment).build();
        tutorService.changeRating(rateDto);
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("SC#getTutorsAndCandidates").build();
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String deleteProfile(@AuthenticationPrincipal Account account) {
        accountService.deleteAccount(account);
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("MC#login").build();
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @RequestMapping(value = "/my_orders", method = RequestMethod.GET)
    public String getStudentOrders(ModelMap map, @AuthenticationPrincipal Account account) {
        Student student = userServiceStudent.findByAccountId(account.getId());
        Optional<List<Order>> orders = orderService.getOrdersByAuthor(student);
        if (orders.isPresent()) {
            map.put("orders", orders.get());
        } else {
            map.put("orders", null);
        }
        return "/views/studentOrdersPage";
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @RequestMapping(value = "/new_order", method = RequestMethod.GET)
    public String addOrderGet(ModelMap map) {
        map.put("subjects", subjectService.findAllTitles());
        map.put("order", new OrderForm());
        return "/views/orderCreationPage";
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @RequestMapping(value = "/new_order", method = RequestMethod.POST)
    public String addOrder(
            RedirectAttributes redirectAttributes,
            @ModelAttribute("order") @Valid OrderForm orderForm,
            BindingResult result,
            ModelMap map, @AuthenticationPrincipal Account account
    ) {
        map.put("subjects", subjectService.findAllTitles());
        if (result.hasErrors()) {
            return "/views/orderCreationPage";
        } else {
            if (orderForm != null) {
                Student student = userServiceStudent.findByAccountId(account.getId());
                OrderConverter orderConverter = new OrderConverter(student);
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
            if (!emailAndPhoneApiValidator.validateEmail(studentForm.getEmail())) {
                redirectAttributes.addFlashAttribute("message", "Your email is not real");
                return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("SC#register").build() + "?status=failed";
            } else if (!emailAndPhoneApiValidator.validatePhone(studentForm.getPhone(), studentForm.getCity().getCountryCode())) {
                redirectAttributes.addFlashAttribute("message", "Your phone is not real or you selected wrong country");
                return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("SC#register").build() + "?status=failed";
            }
            StudentFormToAccountAndStudentConverter converter = new StudentFormToAccountAndStudentConverter();
            Account account = (Account) converter.convert(studentForm, TypeDescriptor.valueOf(StudentForm.class), TypeDescriptor.valueOf(Account.class));
            Student student = (Student) converter.convert(studentForm, TypeDescriptor.valueOf(StudentForm.class), TypeDescriptor.valueOf(Student.class));
            try {
                boolean registered = accountService.registerUser(account, student);
                if (registered) {
                    redirectAttributes.addFlashAttribute("message", "Successfully registered");
                    return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("SC#register").build() + "?status=success";
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
