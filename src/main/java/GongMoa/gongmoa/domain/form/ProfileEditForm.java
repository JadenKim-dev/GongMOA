package GongMoa.gongmoa.domain.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ProfileEditForm {

    @NotBlank
    private String name;

    private MultipartFile picture;

    @Email
    private String email;

}
