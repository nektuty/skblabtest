package ru.tshagaev.skblabtest.controller.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tshagaev.skblabtest.domain.auth.User;
import ru.tshagaev.skblabtest.domain.auth.UserState;
import ru.tshagaev.skblabtest.service.messaging.MessageIdService;
import ru.tshagaev.skblabtest.service.auth.UserService;

import javax.validation.Valid;

/**
 * Контроллер для регистрации/авторизации
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@Slf4j
@Controller
@RequestMapping("auth")
public class AuthController {
    private final UserService service;
    private final MessageIdService messageIdService;

    public AuthController(UserService service, MessageIdService messageIdService) {
        this.service = service;
        this.messageIdService = messageIdService;
    }

    @GetMapping("register")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "auth/regForm";
    }

    @PostMapping("register")
    public String registration(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "auth/regForm";
        }

        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Пароль и подтверждение не совпадают.");
            return "auth/regForm";
        }

        try {
            service.register(user);
        } catch (Exception e) {
            log.info("Ошибка регистрации пользователя " + user.getUsername(), e);
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/regForm";
        }

        messageIdService.sendForApproval(user);

        // Обновляем статус учетки
        user.setState(UserState.ON_APPROVAL);
        service.save(user);

        return "auth/regSuccess";
    }
}
