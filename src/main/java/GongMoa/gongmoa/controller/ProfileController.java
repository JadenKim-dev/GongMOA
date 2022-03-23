package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.OAuth2.LoginUser;
import GongMoa.gongmoa.OAuth2.Role;
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

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final UserService userService;
    private final FileStore fileStore;

    @RequestMapping("/{userId}")
    public String profile(@LoginUser SessionUser user, @PathVariable Long userId, HttpServletRequest request, Model model) {
        if(!user.getId().equals(userId)) {
            return "redirect:/profile/" + user.getId();
        }
        model.addAttribute("profile", new ProfileEditForm(user));
        return "profile";
    }

    @PostMapping("/{userId}/edit")
    public String editProfile(@PathVariable Long userId,
                              @Validated @ModelAttribute("profile") ProfileEditForm form,
                              @LoginUser SessionUser user,
                              HttpSession session,
                              HttpServletRequest request) throws IOException {
        if(!user.getId().equals(userId)) {
            throw new AuthenticationException("본인의 프로필만 고칠 수 있습니다.");
        }
        User currentUser = userService.findUser(userId);
        boolean isEmailChanged = !user.getEmail().equals(form.getEmail());
        if(isEmailChanged && !currentUser.canSendConfirmEmail()) {
//            bindingResult.rejectValue("email", "5분 동안 이메일을 다시 변경할 수 없습니다.");
            request.setAttribute("error.email", "5분 동안 이메일을 다시 변경할 수 없습니다.");
            return "forward:/profile/" + userId;
        }

        UploadFile pictureUploadFile = fileStore.storeFile(form.getPicture());

        User userParam = User.builder()
                .name(form.getName())
                .picture(pictureUploadFile)
                .email(form.getEmail())
                .role(isEmailChanged ? Role.NOT_VALID : null)
                .build();
        userService.updateUser(userId, userParam);

        session.setAttribute("user", new SessionUser(userService.findUser(userId)));

        if(isEmailChanged) {
            return "redirect:/resend-confirm-email";
        }

        return "redirect:/profile/{userId}";
    }

}
