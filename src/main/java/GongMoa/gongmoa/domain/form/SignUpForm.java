package GongMoa.gongmoa.domain.form;

import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {
    @NotBlank
    @Length(min=2, max=20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣A-Za-z0-9_-]{3,20}$")
    private String name;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9]{8,16}", message = "비밀번호는 영어와 숫자를 포함해서 8~16자리 이내로 입력해주세요.")
    private String password;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9]{8,16}", message = "비밀번호는 영어와 숫자를 포함해서 8~16자리 이내로 입력해주세요.")
    private String passwordConfirm;

    @NotBlank
    @Email
    private String email;

}
