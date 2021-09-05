package GongMoa.gongmoa.jsoup;

import GongMoa.gongmoa.service.ContestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlingScheduler {

    private final Crawling crawling;
    private final ContestService contestService;

    @Scheduled(cron = "")
    public void scheduleFixed() {
        crawling.doCrawling();
        deleteExpiredContest();
    }

    public void deleteExpiredContest() {
        contestService.findAllContest().stream().filter(c -> contestService.isExpiredContest(c)).
                forEach(c -> contestService.deleteContest(c));

        log.info("delete contest");
    }
}
