package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.OAuth2.LoginUser;
import GongMoa.gongmoa.OAuth2.SessionUser;
import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.form.SignUpForm;
import GongMoa.gongmoa.service.UserService;
import GongMoa.gongmoa.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final SignUpFormValidator signUpFormValidator;
    private final UserService userService;

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
    public String signUp(@Validated SignUpForm signupForm, BindingResult bindingResult, HttpSession session, Model model) {
        if(bindingResult.hasErrors()) {
            return "user/signup";
        }
        User userParam = User.builder()
                .name(signupForm.getName())
                .email(signupForm.getEmail())
                .password(signupForm.getPassword())
                .build();
        User user = userService.save(userParam);

        userService.sendValidateEmail(user);

        userService.login(user);
        session.setAttribute("user", new SessionUser(user));
        model.addAttribute("title", "이메일을 인증해주세요");
        model.addAttribute("email", user.getEmail());
        return "user/validate-email";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(
            @RequestParam("token") String token,
            @RequestParam("email") String email,
            Model model,
            HttpSession session) {

        String view = "user/validated-email";

        try{
            User user = userService.findByEmail(email);
            if(!user.isValidToken(token)) {
                model.addAttribute("error", "wrong.token");
                return view;
            }

            userService.completeSignUp(user);
            session.setAttribute("user", new SessionUser(user));
        } catch (NoSuchElementException e) {
            model.addAttribute("error", "wrong.email");
        }

        return view;
    }
    
    @RequestMapping("/resend-confirm-email")
    public String resendConfirmEmail(@LoginUser SessionUser user, Model model) {
        model.addAttribute("email", user.getEmail());

        if(!userService.findUser(user.getId()).canSendConfirmEmail()) {
            model.addAttribute("error", "인증 이메일은 5분에 한번만 전송 가능합니다.");
            return "user/validate-email";
        }

//        User currentUser = userService.findUser(user.getId());
        userService.generateEmailCheckToken(user.getId());
        userService.sendValidateEmail(userService.findUser(user.getId()));

        model.addAttribute("title", "인증 이메일을 다시 전송하였습니다.");
        return "user/validate-email";
    }
    
    
}
