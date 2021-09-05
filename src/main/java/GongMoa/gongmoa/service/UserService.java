package GongMoa.gongmoa.service;

import GongMoa.gongmoa.OAuth2.Role;
import GongMoa.gongmoa.OAuth2.SessionUser;
import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.OAuth2.UserRepository;
import GongMoa.gongmoa.OAuth2.security.UserAccount;
import GongMoa.gongmoa.domain.form.SignUpForm;
import GongMoa.gongmoa.fileupload.UploadFile;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;
    private final HttpSession httpSession;

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public void updateUser(Long userId, User userParam) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        user.update(userParam.getName(), userParam.getPicture()!=null ? userParam.getPicture() : user.getPicture());
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
                .role(Role.USER)
                .build();
        return userRepository.save(newUser);
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
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
