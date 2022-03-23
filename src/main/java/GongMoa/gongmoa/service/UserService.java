package GongMoa.gongmoa.service;

import GongMoa.gongmoa.OAuth2.Role;
import GongMoa.gongmoa.OAuth2.SessionUser;
import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.OAuth2.UserRepository;
import GongMoa.gongmoa.OAuth2.security.UserAccount;
import GongMoa.gongmoa.config.AppProperties;
import GongMoa.gongmoa.domain.form.SignUpForm;
import GongMoa.gongmoa.fileupload.UploadFile;
import GongMoa.gongmoa.mail.EmailMessage;
import GongMoa.gongmoa.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Template;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession httpSession;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;
    private final MailService mailService;

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public void updateUser(Long userId, User userParam) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        user.update(
                userParam.getName(),
                userParam.getPicture()!=null ? userParam.getPicture() : user.getPicture(),
                userParam.getEmail(),
                userParam.getRole()!=null ? userParam.getRole() : user.getRole());
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
    }

    public User findByName(String name) {
        return userRepository.findByName(name).orElse(null);
    }

    // login/signup 기능
    public User save(User userParam) {
        User newUser = User.builder()
                .name(userParam.getName())
                .email(userParam.getEmail())
                .password(passwordEncoder.encode(userParam.getPassword()))
                .picture(new UploadFile("basic.JPG", "basic.JPG"))
                .role(Role.NOT_VALID)
                .build();
        newUser.generateEmailCheckToken();
        return userRepository.save(newUser);
    }

    @Transactional
    public void generateEmailCheckToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        user.generateEmailCheckToken();
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        httpSession.setAttribute("user", new SessionUser(user.get()));
        return new UserAccount(user.get());
    }

    public void login(User user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(user),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRoleKey()))
        );
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public void sendValidateEmail(User user) {
        Context context = new Context();
        context.setVariable("link",
                "/check-email-token?token=" + user.getEmailCheckToken() + "&email=" + user.getEmail());
        context.setVariable("nickname", user.getName());
        context.setVariable("linkName", "이메일 인증");
        context.setVariable("message", "아래 링크를 클릭하여 이메일을 인증해주세요.");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(user.getEmail())
                .subject("공모아, 로그인 링크")
                .message(message)
                .build();
        mailService.sendMail(emailMessage);

    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
    }

    public void completeSignUp(User user) {
        user.completeSignUp();
        login(user);
    }
}
