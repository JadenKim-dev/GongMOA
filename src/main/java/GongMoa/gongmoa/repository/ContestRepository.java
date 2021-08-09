package GongMoa.gongmoa.repository;

import GongMoa.gongmoa.domain.Contest.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
}
