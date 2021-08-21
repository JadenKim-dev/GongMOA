package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.OAuth2.LoginUser;
import GongMoa.gongmoa.OAuth2.SessionUser;
import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.service.ContestService;
import GongMoa.gongmoa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
//@ResponseBody
@RequestMapping("/contests")
@RequiredArgsConstructor
@Slf4j
public class ContestController {
    private final ContestService contestService;

    @GetMapping
    public String listContests(@RequestParam(required = false) String title, Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("userName", user.getName());
        }

        List<Contest> contests;
        if (title!=null) {
            contests = contestService.searchContestByTitle(title);
        } else {
            contests = contestService.findAllContest();
        }
        model.addAttribute("contests", contests);
        return "contests";
    }

    @GetMapping("/{contestId}")
    public String contest(@PathVariable long contestId, Model model) {
        Contest contest = contestService.findContest(contestId);
        List<Contest> currentContests = contestService.findAllContest();

        model.addAttribute("contest", contest);
        model.addAttribute("currentContests", currentContests);

        return "contest";
    }
}