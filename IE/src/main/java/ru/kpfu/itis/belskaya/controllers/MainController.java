package ru.kpfu.itis.belskaya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.belskaya.exceptions.NotFoundException;
import ru.kpfu.itis.belskaya.models.Rate;
import ru.kpfu.itis.belskaya.models.Tutor;
import ru.kpfu.itis.belskaya.models.forms.LoginForm;
import ru.kpfu.itis.belskaya.services.TutorService;


import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


/**
 * @author Elizaveta Belskaya
 */
@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private TutorService tutorService;

    @RequestMapping(value = "/main")
    public String login(@RequestParam(required = false) String status,
                        @Valid @ModelAttribute("loginForm") LoginForm loginForm,
                        BindingResult result,
                        ModelMap map) {
        if (status != null && status.equals("failed")) {
            map.put("error", "User not found");
        } else {
            map.put("error", null);
        }
        return "/views/mainPage";
    }

    @RequestMapping(value = "/tutor_profile/{id}", method = RequestMethod.GET)
    public String getTutorProfile(@PathVariable("id") Long id, ModelMap map) {
        Optional<Tutor> tutor = tutorService.getTutorById(id);
        if (tutor.isPresent()) {
            Optional<List<Rate>> reviewsList = tutorService.getRatesOfTutor(tutor.get());
            map.put("account", tutor.get().getAccount());
            map.put("tutor", tutor.get());
            if (reviewsList.isPresent()) {
                map.put("reviewsList", reviewsList.get());
            } else {
                map.put("reviewsList", null);
            }
            return "/views/tutorProfileForAll";
        } else {
            throw new NotFoundException();
        }
    }

    @GetMapping(value = "/forbidden")
    public String forbidden(ModelMap map) {
        map.put("status", HttpStatus.FORBIDDEN.value());
        map.put("alert", "403: you are not allowed to access this page");
        return "/views/errorPage";
    }


}
