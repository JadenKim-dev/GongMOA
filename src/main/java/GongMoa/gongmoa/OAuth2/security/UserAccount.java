package GongMoa.gongmoa.OAuth2.security;


import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserAccount extends User {

    private GongMoa.gongmoa.OAuth2.User user;

    public UserAccount(GongMoa.gongmoa.OAuth2.User user) {
        super(user.getName(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRoleKey())));
        this.user = user;
    }
}
