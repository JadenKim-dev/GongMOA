package GongMoa.gongmoa.domain.Contest;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class ApplicationDate {
    private LocalDateTime applicationStartDateTime;
    private LocalDateTime applicationEndDateTime;
}
