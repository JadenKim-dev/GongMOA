package GongMoa.gongmoa.domain.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ProfileEditForm {

    @NotBlank
    private String name;

    private MultipartFile picture;

    @Email
    private String email;

}
