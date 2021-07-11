package ru.tshagaev.skblabtest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер стартовой страницы приложения
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@Controller
public class IndexController {

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("appName", appName);
        return "index";
    }
}
