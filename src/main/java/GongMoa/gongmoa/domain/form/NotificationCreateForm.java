package GongMoa.gongmoa.domain.form;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
public class NotificationCreateForm {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
}
