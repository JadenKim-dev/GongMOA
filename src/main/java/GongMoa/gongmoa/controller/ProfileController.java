package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.OAuth2.LoginUser;
import GongMoa.gongmoa.OAuth2.SessionUser;
import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.form.ProfileEditForm;
import GongMoa.gongmoa.fileupload.FileStore;
import GongMoa.gongmoa.fileupload.UploadFile;
import GongMoa.gongmoa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final UserService userService;
    private final FileStore fileStore;

    @GetMapping("/{userId}")
    public String profile(@LoginUser SessionUser user, @PathVariable Long userId, Model model) {
        if(!user.getId().equals(userId)) {
            return "redirect:/profile/" + user.getId();
        }
        User currentUser = userService.findUser(user.getId());

        model.addAttribute("profile", currentUser);

        return "profile";
    }

    @PostMapping("/{userId}/edit")
    public String editProfile(@PathVariable Long userId,
                              HttpSession session,
                              @Validated @ModelAttribute("profile") ProfileEditForm form,
                              BindingResult bindingResult) throws IOException {
//        if(bindingResult.hasErrors()) {
//            log.info("bindingResult.hasErrors(): True");
//            return "profile";
//        }

//        log.info("form.getName={}", form.getName());
//        log.info("form.getEmail={}", form.getEmail());
//        log.info("form.getPicture={}", form.getPicture());

        UploadFile pictureUploadFile = fileStore.storeFile(form.getPicture());

        User userParam = new User();
        userParam.update(form.getName(), pictureUploadFile);

        userService.updateUser(userId, userParam);
        session.setAttribute("user", new SessionUser(userService.findUser(userId)));

        return "redirect:/profile/{userId}";
    }

}
