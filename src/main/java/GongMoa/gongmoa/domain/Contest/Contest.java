package GongMoa.gongmoa.domain.Contest;

import GongMoa.gongmoa.domain.BaseEntity.DateBaseEntity;
import GongMoa.gongmoa.domain.Comment;
import lombok.Getter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class Contest extends DateBaseEntity {
    @Id @GeneratedValue
    private Long id;

    private String title;

    @Lob @Basic(fetch = LAZY)
    private String description;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    @Embedded
    private ApplicationDate applicationDate;

    @Embedded
    private Host host;

    @Embedded
    private Prize prize;

    @OneToMany(mappedBy = "contest")
    private List<Comment> comments = new ArrayList<>();

    public Contest() {

    }

    public Contest(String title, String description, String image, ApplicationDate applicationDate, Host host, Prize prize) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.applicationDate = applicationDate;
        this.host = host;
        this.prize = prize;
    }
}
