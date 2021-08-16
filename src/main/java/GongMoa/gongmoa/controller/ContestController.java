package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.service.ContestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
//@ResponseBody
@RequestMapping("/contests")
@RequiredArgsConstructor
@Slf4j
public class ContestController {
    private final ContestService contestService;

    @GetMapping
    public String contests(@RequestParam(required = false) String title, Model model) {
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
        Contest contest = contestService.findContest(contestId).orElseThrow(NoSuchElementException::new);
        model.addAttribute("contest", contest);
        return "contest";
    }
}
