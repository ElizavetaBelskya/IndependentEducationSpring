package ru.kpfu.itis.belskaya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.kpfu.itis.belskaya.models.Account;
import ru.kpfu.itis.belskaya.repositories.CityRepository;

/**
 * @author Elizaveta Belskaya
 */
@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private CityRepository cityRepository;

    @GetMapping
    public String mainPage() {
        return "/views/mainPage";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap map) {
        map.put("user", new Account());
        return "/views/mainPage";
    }

}
