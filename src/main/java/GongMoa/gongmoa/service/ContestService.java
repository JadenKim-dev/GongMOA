package GongMoa.gongmoa.service;

import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    public boolean isExpiredContest(Contest contest) {
        if(contest.getApplicationDate().getApplicationEndDate().isBefore(LocalDate.now()))
            return true;

        return false;
    }
}
