package ru.kpfu.itis.belskaya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kpfu.itis.belskaya.models.*;
import ru.kpfu.itis.belskaya.models.forms.TutorForm;
import ru.kpfu.itis.belskaya.services.*;
import ru.kpfu.itis.belskaya.validators.EmailAndPhoneValidator;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Elizaveta Belskaya
 */
@Controller
@RequestMapping("/tutor")
public class TutorController {

    private final String TUTOR = "_____Tutor";

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

    @RequestMapping(value = "/orders", method = RequestMethod.GET, params = {"page"})
    public String orders(@RequestParam Integer page, ModelMap map) {
        List<Order> orders = orderService.getOrdersByPage(page);
        System.out.println(orders);
        map.put("orders", orders);
        map.put("page", page);
        map.put("countOfPages", orderService.getCountOfPages());
        return "/views/newOrdersPage";
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String orders(ModelMap map) {
        int page = 1;
        List<Order> orders = orderService.getOrdersByPage(page);
        map.put("orders", orders);
        map.put("page", page);
        map.put("countOfPages", orderService.getCountOfPages());
        return "/views/newOrdersPage";
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
                return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("SC#register").build() + "?status=failed";
            } else if (!emailAndPhoneValidator.validatePhone(tutorForm.getPhone(), tutorForm.getCity().getCountryCode())) {
                redirectAttributes.addFlashAttribute("message", "Your phone is not real or you selected wrong country");
                return "redirect:"+ MvcUriComponentsBuilder.fromMappingName("SC#register").build() + "?status=failed";
            }

            Account account = Account.builder()
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
                    .account(account).build();
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
