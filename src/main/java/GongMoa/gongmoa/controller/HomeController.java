package GongMoa.gongmoa.controller;


import GongMoa.gongmoa.fileupload.FileStore;
import GongMoa.gongmoa.jsoup.Crawling;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import GongMoa.gongmoa.OAuth2.LoginUser;
import GongMoa.gongmoa.OAuth2.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("/proxy")
    public String login(HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referer);

        log.info("referer={}", referer);

        return "redirect:/oauth2/authorization/google";
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

    @GetMapping("/jsoup")
    @ResponseBody
    public String jsoup() {
        crawling.doCrawling();
        return "ok";
    }

    @GetMapping("/practice")
    public String chat() {
        return "chat/practice";
    }
}

