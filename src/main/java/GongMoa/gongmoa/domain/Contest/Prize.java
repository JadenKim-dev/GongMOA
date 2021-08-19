package GongMoa.gongmoa.domain.Contest;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Prize {
    private String firstPrize;
    private String totalPrize;
    private String benefit;

    public Prize() {
    }

    public Prize(String firstPrize, String totalPrize, String benefit) {
        this.firstPrize = firstPrize;
        this.totalPrize = totalPrize;
        this.benefit = benefit;
    }
}
