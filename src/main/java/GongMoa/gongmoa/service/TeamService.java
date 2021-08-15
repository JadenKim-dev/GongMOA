package GongMoa.gongmoa.service;

import GongMoa.gongmoa.domain.Contest.Contest;
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
    public Long createTeam(Member leader, Contest contest) {
        Team team = new Team(contest);
        Participation participation = new Participation(leader, team, true);
        team.getParticipants().add(participation);

        return teamRepository.save(team).getId();
    }

    @Transactional
    public void deleteTeam(Team team) {
        teamRepository.delete(team);
        return;
    }

    @Transactional
    public void join(Member member, Team team) {
        Participation participation = new Participation(member, team, false);
        team.getParticipants().add(participation);
        return;
    }

    @Transactional
    public void leaveTeam(long participationId, Team team) {
        Participation delParticipation = findParticipationFromTeam(team, participationId);
        team.getParticipants().removeIf(targetParticipation -> targetParticipation.equals(delParticipation));
    }

    public Optional<Team> findTeam(Long teamId) {
        return teamRepository.findById(teamId);
    }


    // 유틸리티 메서드
    private Participation findParticipationFromTeam(Team team, Long participationId) {
        List<Participation> participants = team.getParticipants();
        for (Participation participant : participants) {
            if(participant.getId().equals(participationId)) {
                return participant;
            }
        } return null;
    }

}
