package GongMoa.gongmoa.domain.BaseEntity;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class DateBaseEntity {
    private LocalDateTime createdDateTime;
    private LocalDateTime closedDateTime;
}
