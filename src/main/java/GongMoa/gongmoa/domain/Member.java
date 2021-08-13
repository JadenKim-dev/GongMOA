package GongMoa.gongmoa.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;
    private String email;

    private LocalDateTime createdTime;


//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<Participation> participations = new ArrayList<>();

//    @OneToMany
//    private List<Registration> registrations = new ArrayList<>();


    public Member() {
    }

    public Member(String username, String password, String email, LocalDateTime createdTime) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdTime = createdTime;
    }
}
