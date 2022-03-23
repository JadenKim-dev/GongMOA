package GongMoa.gongmoa.controller;


import GongMoa.gongmoa.fileupload.FileStore;
import GongMoa.gongmoa.jsoup.Crawling;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final FileStore fileStore;
    private final Crawling crawling;

    @GetMapping("/")
    public String home() {
        return "redirect:/contests";
    }

    @RequestMapping("/login-form")
    public String login(HttpServletRequest request) {
        if (request.getAttribute("loginFailMsg") == null) {
            String referer = request.getHeader("Referer");
            request.getSession().setAttribute("prevPage", referer);
        }

        return "login-form";
    }

    @GetMapping("/login-failure")
    public String loginFail(HttpServletRequest request, BindingResult bindingResult) {
        bindingResult.reject("loginFail", null, (String) request.getAttribute("loginFail"));
        return "";
    }

    @GetMapping("/error/500")
    public void error(HttpServletResponse response) throws IOException {
        response.sendError(500);
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @RequestMapping("/errors/not-validated")
    public String notValidatedPage(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        return "error/not-validated";
    }

    @GetMapping("/crawling")
    public void crowaling() {
        crawling.doCrawling();
    }
}
