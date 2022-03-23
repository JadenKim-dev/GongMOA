package GongMoa.gongmoa.OAuth2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "손님"),
    NOT_VALID("ROLE_NOT_VALIDATED", "인증되지 않은 사용자"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;
}


