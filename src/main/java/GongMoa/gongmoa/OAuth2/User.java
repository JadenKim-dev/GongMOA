package GongMoa.gongmoa.OAuth2;

import GongMoa.gongmoa.domain.Comment;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.domain.Like;
import GongMoa.gongmoa.domain.Notification;
import GongMoa.gongmoa.domain.Registration;
import GongMoa.gongmoa.domain.chat.ChatRoomJoin;
import GongMoa.gongmoa.domain.form.ChatMessageForm;
import GongMoa.gongmoa.fileupload.UploadFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static javax.persistence.CascadeType.*;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne(cascade = ALL)
    @JoinColumn(name="uploadfile_id")
    private UploadFile picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Registration> registrations = new ArrayList<>();

    private String emailCheckToken;

    private LocalDateTime emailCheckTokenGeneratedAt;

    private boolean emailVerified;

    private LocalDateTime joinedAt;

    @Builder
    public User(String name, String email, String password, UploadFile picture, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.role = role;
    }

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<ChatRoomJoin> chatRoomJoins = new ArrayList<>();

    public User update(String name, UploadFile picture, String email, Role role) {
        this.name = name;
        this.picture = picture;
        this.email = email;
        this.role = role;
        return this;
    }

    public Like findLikeByContest(Contest contest) {
        return likes.stream().filter(l -> l.getContest() == contest).findFirst().orElse(null);
    }

    public Like findLikeByNotification(Notification notification) {
        return likes.stream().filter(l -> l.getNotification() == notification).findFirst().orElse(null);
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusSeconds(30));
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
        this.role = Role.USER;
    }
}
