package ru.kpfu.itis.belskaya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import ru.kpfu.itis.belskaya.validators.EmailAndPhoneApiValidator;

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
    private EmailAndPhoneApiValidator emailAndPhoneApiValidator;

    @Autowired
    private StudentService studentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CityService cityService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TutorService tutorService;

    @PreAuthorize("hasAuthority('TUTOR')")
    @RequestMapping(value = "/my_students", method = RequestMethod.GET)
    public String getMyStudents(ModelMap map,  @AuthenticationPrincipal Account account) {
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

    @PreAuthorize("hasAuthority('TUTOR')")
    @RequestMapping(value = "/my_students", method = RequestMethod.POST)
    public String getStudentOfTutorPost(@RequestParam("reject") Long orderId, @AuthenticationPrincipal Account account) {
        Tutor tutor = userServiceTutor.findByAccount_Id(account.getId());
        orderService.cancelTutor(orderId, tutor);
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("TC#getMyStudents").build() + "?rejected=true";
    }

    @PreAuthorize("hasAuthority('TUTOR')")
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String getOrders(ModelMap map,  @AuthenticationPrincipal Account account) {
        Tutor tutor = userServiceTutor.findByAccount_Id(account.getId());
        Optional<List<Order>> orders = orderService.getSuitableOrders(tutor.getId());
        if (orders.isPresent()) {
            map.put("orders", orders.get());
        } else {
            map.put("orders", null);
        }
        return "/views/newOrdersPage";
    }

    @PreAuthorize("hasAuthority('TUTOR')")
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfile(ModelMap map,  @AuthenticationPrincipal Account account) {
        Tutor tutor = userServiceTutor.findByAccount_Id(account.getId());
        Optional<List<Rate>> reviewsList = tutorService.getRatesOfTutor(tutor);
        map.put("account", account);
        map.put("tutor", tutor);
        map.put("studentCount", studentService.getStudentsCountByTutor(tutor));
        map.put("mapSubjectToAmount", tutorService.getMapSubjectToStudentsAmount(tutor));
        if (reviewsList.isPresent()) {
            map.put("reviewsList", reviewsList.get());
        } else {
            map.put("reviewsList", null);
        }
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
            if (!emailAndPhoneApiValidator.validateEmail(tutorForm.getEmail())) {
                redirectAttributes.addFlashAttribute("message", "Your email is not real");
                return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("TC#register").build() + "?status=failed";
            } else if (!emailAndPhoneApiValidator.validatePhone(tutorForm.getPhone(), tutorForm.getCity().getCountryCode())) {
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
                    return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("TC#register").build() + "?status=success";
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

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String deleteProfile( @AuthenticationPrincipal Account account) {
        accountService.deleteAccount(account);
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("MC#login").build();
    }



}
