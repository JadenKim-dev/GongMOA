package GongMoa.gongmoa.domain.Contest;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.time.LocalDate;


@Embeddable
@Getter
public class ApplicationDate {
    private LocalDate applicationStartDate;
    private LocalDate applicationEndDate;


    public ApplicationDate() {

    }

    public ApplicationDate(LocalDate applicationStartDate, LocalDate applicationEndDate) {
        this.applicationStartDate = applicationStartDate;
        this.applicationEndDate = applicationEndDate;
    }
}
