package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.OAuth2.SessionUser;
import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.form.SignUpForm;
import GongMoa.gongmoa.service.UserService;
import GongMoa.gongmoa.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final SignUpFormValidator signUpFormValidator;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "user/signup";
    }

    @PostMapping("/signup")
    public String signUp(@Validated SignUpForm signupForm, HttpSession session, Errors errors) {
        if(errors.hasErrors()) {
            return "user/signup";
        }
        User userParam = modelMapper.map(signupForm, User.class);
        User user = userService.save(userParam);

        userService.login(user);
        session.setAttribute("user", new SessionUser(user));

        return "redirect:/";
    }
}
