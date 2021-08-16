package GongMoa.gongmoa.domain.Contest;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Host {
    private String organizer;
    private String manager;
    private String sponsor;

    public Host() {
    }

    public Host(String organizer, String manager, String sponsor) {
        this.organizer = organizer;
        this.manager = manager;
        this.sponsor = sponsor;
    }
}
