package ru.kpfu.itis.belskaya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.belskaya.converters.TutorFormToAccountAndTutorConverter;
import ru.kpfu.itis.belskaya.models.*;
import ru.kpfu.itis.belskaya.models.forms.TutorForm;
import ru.kpfu.itis.belskaya.services.*;
import ru.kpfu.itis.belskaya.validators.EmailAndPhoneValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author Elizaveta Belskaya
 */
@Controller
@RequestMapping("/tutor")
public class TutorController {

    @Autowired
    private UserService<Tutor> userServiceTutor;

    @Autowired
    private EmailAndPhoneValidator emailAndPhoneValidator;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CityService cityService;

    @Autowired
    private AccountService accountService;



    @RequestMapping(value = "/my_students", method = RequestMethod.GET)
    public String getMyStudents(ModelMap map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        Tutor tutor = userServiceTutor.findByAccount_Id(account.getId());
        Optional<List<Order>> tutorOrders = orderService.getOrdersByTutor(tutor);
        if (tutorOrders.isPresent()) {
            List<Student> students = tutorOrders.get().stream().map(Order::getAuthor).collect(Collectors.toList());
            List<Account> accounts = students.stream().map(Student::getAccount).collect(Collectors.toList());
            map.put("orders", tutorOrders.get());
            map.put("students", students);
            map.put("accounts", accounts);
        } else {
            map.put("orders", null);
        }
        return "/views/studentOfTutorPage";
    }

    @RequestMapping(value = "/my_students", method = RequestMethod.POST)
    public String getStudentOfTutorPatch(@RequestParam("reject") Long orderId) {
        orderService.cancelTutor(orderId);
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("TC#getMyStudents").build() + "?rejected=true";
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String orders(ModelMap map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        Tutor tutor = userServiceTutor.findByAccount_Id(account.getId());
        Optional<List<Order>> orders = orderService.getSuitableOrders(tutor.getId());
        if (orders.isPresent()) {
            map.put("orders", orders.get());
        } else {
            map.put("orders", null);
        }
        return "/views/newOrdersPage";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfile(ModelMap map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        Tutor tutor = userServiceTutor.findByAccount_Id(account.getId());
        map.put("account", account);
        map.put("tutor", tutor);
        return "/views/tutorProfilePage";
    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(ModelMap map) {
        map.put("userForm", new TutorForm());
        map.put("cities", cityService.findAll());
        map.put("subjects", subjectService.findAllSubjects());
        return "/views/tutorRegistrationPage";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPost(RedirectAttributes redirectAttributes,
                               @ModelAttribute("userForm") @Valid TutorForm tutorForm,
                               BindingResult result,
                               ModelMap map) {
        if (!result.hasErrors()) {
            if (!emailAndPhoneValidator.validateEmail(tutorForm.getEmail())) {
                redirectAttributes.addFlashAttribute("message", "Your email is not real");
                return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("TC#register").build() + "?status=failed";
            } else if (!emailAndPhoneValidator.validatePhone(tutorForm.getPhone(), tutorForm.getCity().getCountryCode())) {
                redirectAttributes.addFlashAttribute("message", "Your phone is not real or you selected wrong country");
                return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("TC#register").build() + "?status=failed";
            }

            TutorFormToAccountAndTutorConverter converter = new TutorFormToAccountAndTutorConverter();
            Account account = (Account) converter.convert(tutorForm, TypeDescriptor.valueOf(TutorForm.class), TypeDescriptor.valueOf(Account.class));
            Tutor tutor = (Tutor) converter.convert(tutorForm, TypeDescriptor.valueOf(TutorForm.class), TypeDescriptor.valueOf(Tutor.class));
            try {
                boolean registered = accountService.registerUser(account, tutor);
                if (registered) {
                    redirectAttributes.addFlashAttribute("message", "Successfully registered");
                    return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("TC#register").build();
                } else {
                    redirectAttributes.addFlashAttribute("message", "Registration failed");
                    return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("TC#register").build() + "?status=failed";
                }
            } catch (Exception ex) {
                redirectAttributes.addFlashAttribute("message", ex.getMessage());
                return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("TC#register").build() + "?status=failed";
            }
        }

        List<City> cityList = cityService.findAll();
        map.put("cities", cityList);
        map.put("subjects", subjectService.findAllSubjects());
        return "/views/tutorRegistrationPage";
    }



}
