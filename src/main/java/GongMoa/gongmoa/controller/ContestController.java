package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.OAuth2.LoginUser;
import GongMoa.gongmoa.OAuth2.SessionUser;
import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.CommentForm;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.service.CommentService;
import GongMoa.gongmoa.service.ContestService;
import GongMoa.gongmoa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
//@ResponseBody
@RequestMapping("/contests")
@RequiredArgsConstructor
@Slf4j
public class ContestController {
    private final ContestService contestService;
    private final UserService userService;
    private final CommentService commentService;

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
        model.addAttribute("commentForm", new CommentForm());

        return "contest";
    }

    @GetMapping("/{contestId}/comments")
    public String comment(@PathVariable long contestId,
                          @ModelAttribute("commentForm") CommentForm commentForm,
                          Model model) {
        Contest contest = contestService.findContest(contestId);
        model.addAttribute("contest", contest);
        model.addAttribute("commentForm", commentForm);
        return "comment";
    }

    @PostMapping("/{contestId}/comments")
    public String writeComment(@PathVariable("contestId") Long contestId,
                               @LoginUser SessionUser user,
                               @ModelAttribute("commentForm") CommentForm commentForm,
                               HttpServletRequest request) {

        User currentUser = userService.findUser(user.getId());
        Contest contest = contestService.findContest(contestId);


        System.out.println("contest = " + contest);
        commentService.createComment(currentUser, contest, commentForm.getContent());

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}