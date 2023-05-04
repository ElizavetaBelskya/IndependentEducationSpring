package ru.kpfu.itis.belskaya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.belskaya.models.forms.LoginForm;
import ru.kpfu.itis.belskaya.validators.EmailAndPhoneValidator;


import javax.validation.Valid;


/**
 * @author Elizaveta Belskaya
 */
@Controller
@RequestMapping("/")
public class MainController {

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

}
