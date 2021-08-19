package GongMoa.gongmoa.domain.form;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NotificationCreateForm {
  
    @NotBlank
    private String title;
    @NotBlank
    private String description;
}
