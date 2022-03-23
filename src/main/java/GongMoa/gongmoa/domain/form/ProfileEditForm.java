package GongMoa.gongmoa.domain.form;

import GongMoa.gongmoa.OAuth2.SessionUser;
import GongMoa.gongmoa.OAuth2.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ProfileEditForm {

    @NotBlank
    @Length(min=2, max=20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣A-Za-z0-9]{3,20}$", message = "이름은 한글, 영어, 숫자를 포함해서 3~20자로 입력해주세요.")
    private String name;


    private MultipartFile picture;

    @NotBlank
    @Email
    private String email;

    public ProfileEditForm() {
    }

    public ProfileEditForm(SessionUser user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
