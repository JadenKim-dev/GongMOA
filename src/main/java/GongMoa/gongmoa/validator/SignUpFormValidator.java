package GongMoa.gongmoa.validator;

import GongMoa.gongmoa.OAuth2.UserRepository;
import GongMoa.gongmoa.domain.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) target;
        if(userRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()}, "이미 사용 중인 이메일입니다.");
        }

        if(userRepository.existsByName(signUpForm.getName())) {
            errors.rejectValue("name", "invalid.name", new Object[]{signUpForm.getEmail()}, "이미 사용중인 이름입니다.");
        }

        if(!signUpForm.getPassword().equals(signUpForm.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "wrong.password", "입력한 패스워드가 일치하지 않습니다.");
        }

    }
}
