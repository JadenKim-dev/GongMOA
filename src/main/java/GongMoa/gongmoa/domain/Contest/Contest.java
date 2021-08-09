package GongMoa.gongmoa.domain.Contest;

import GongMoa.gongmoa.domain.BaseEntity.DateBaseEntity;
import lombok.Getter;

import javax.persistence.*;

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


}
