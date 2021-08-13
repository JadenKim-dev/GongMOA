package GongMoa.gongmoa;

import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final ContestRepository contestRepository;

    @PostConstruct
    public void init() {
        contestRepository.save(new Contest("첫번째 공모전", "1", null, null, null, null));
        contestRepository.save(new Contest("두번째 공모전", "2", null, null, null, null));
        contestRepository.save(new Contest("세번째 공모전", "3", null, null, null, null));
    }

}
