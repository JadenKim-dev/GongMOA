package GongMoa.gongmoa.repository;

import GongMoa.gongmoa.domain.Contest.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {

    List<Contest> findByTitleContaining(String title);
}
