package GongMoa.gongmoa.service;

import GongMoa.gongmoa.domain.Member;
import GongMoa.gongmoa.domain.Participation;
import GongMoa.gongmoa.domain.Team;
import GongMoa.gongmoa.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public Long createTeam(Team team) {
        return teamRepository.save(team).getId();
    }

    @Transactional
    public void deleteTeam(Team team) {
        teamRepository.delete(team);
        return;
    }

    public Optional<Team> findTeam(Long teamId) {
        return teamRepository.findById(teamId);
    }

    @Transactional
    public void joinTeam(Member member, Team team) {
        team.addParticipant(member);
        return;
    }

    @Transactional
    public void leaveTeam(Participation participant, Team team) {
        team.deleteParticipant(participant);
    }
}
