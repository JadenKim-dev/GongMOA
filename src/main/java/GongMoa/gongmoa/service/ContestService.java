package GongMoa.gongmoa.service;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.domain.Like;
import GongMoa.gongmoa.domain.LikeType;
import GongMoa.gongmoa.domain.Notification;
import GongMoa.gongmoa.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ContestService {

    private final ContestRepository contestRepository;

    @Transactional
    public Long createContest(Contest contest) {
        return contestRepository.save(contest).getId();
    }

    @Transactional
    public void createContests(List<Contest> contests) {
        contestRepository.saveAll(contests);
    }

    @Transactional
    public void deleteContest(Contest contest) {
        contestRepository.delete(contest);
        return;
    }

    public Contest findContest(Long contestId) {
        return contestRepository.findById(contestId).orElseThrow(NoSuchElementException::new);
    }

    public List<Contest> findAllContest() {
        return contestRepository.findAll();
    }

    public List<Contest> searchContestByTitle(String title) {
        return contestRepository.findByTitleContaining(title);
    }


    @Transactional
    public void likeContest(User user, Contest contest) {
        Like like = new Like(user, LikeType.CONTEST, contest, null);
        contest.getLikes().add(like);
    }

    @Transactional
    public void cancelLikeInContest(User user, Contest contest) {
        Like like = contest.findLikeByUser(user);
        contest.getLikes().remove(like);
    }

    public boolean isExpiredContest(Contest contest) {
        if(contest.getApplicationDate().getApplicationEndDate().isBefore(LocalDate.now()))
            return true;

        return false;
    }
}
