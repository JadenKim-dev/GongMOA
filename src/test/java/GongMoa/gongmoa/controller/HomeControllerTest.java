package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.OAuth2.UserRepository;
import GongMoa.gongmoa.domain.form.SignUpForm;
import GongMoa.gongmoa.infra.MockMvcTest;
import GongMoa.gongmoa.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@MockMvcTest
public class HomeControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired MockHttpSession session;
    @Autowired UserRepository userRepository;
    @Autowired UserService userService;


    @BeforeEach
    void beforeEach() {
        User userParam = User.builder()
                .name("jaden")
                .email("jaden@gmail.com")
                .password("abcde12345")
                .build();
        userService.save(userParam);
    }

    @DisplayName("로그인 성공")
    @Test
    void login_success() throws Exception {
        mockMvc.perform(post("/doLogin")
                    .param("username", "jaden@gmail.com")
                    .param("password", "abcde12345")
                    .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("jaden"));
    }

    @DisplayName("로그인 실패")
    @Test
    void login_failure() throws Exception {
        mockMvc.perform(post("/doLogin")
                        .param("username", "xxxxx@xxx.com")
                        .param("password", "xxxxx12345")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/login-form"))
                .andExpect(unauthenticated());
    }

}
