package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.OAuth2.UserRepository;
import GongMoa.gongmoa.infra.MockMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.web.servlet.function.RequestPredicates.param;


@MockMvcTest
class UserControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;

    @DisplayName("회원 가입 폼 출력")
    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/signup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/signup"))
                .andExpect(unauthenticated());
    }
    
    @DisplayName("회원 가입 처리 - 입력값 정상")
    @Test
    void signUpSubmit_with_correct_input() throws Exception {
        mockMvc.perform(post("/signup")
                .param("name", "jaden")
                .param("email", "aaa@email.com")
                .param("password", "abcde12345")
                .param("passwordConfirm", "abcde12345")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated().withUsername("jaden"));

        User user = userRepository.findByEmail("aaa@email.com").orElse(null);
        assertNotNull(user);
        assertNotEquals(user.getPassword(), "abcde12345");
    }

    @DisplayName("회원 가입 처리 - 입력값 오류")
    @Test
    void signUpSubmit_with_wrong_input() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("name", "jaden")
                        .param("email", "12345")
                        .param("password", "abcde12345")
                        .param("passwordConfirm", "abcde12345")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/signup"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 처리 - 비밀번호 다르게 입력")
    @Test
    void signUpSubmit_with_different_password() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("name", "jaden")
                        .param("email", "aaa@email.com")
                        .param("password", "abcde12345")
                        .param("passwordConfirm", "xxxxx00000")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/signup"))
                .andExpect(unauthenticated());
    }




}