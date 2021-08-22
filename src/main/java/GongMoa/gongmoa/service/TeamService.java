package GongMoa.gongmoa.service;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.domain.Notification;
import GongMoa.gongmoa.domain.Participation;
import GongMoa.gongmoa.domain.Team;
import GongMoa.gongmoa.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public Long createTeam(User leader, Contest contest, Notification notification) {
        Team team = new Team(contest, notification);
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
    public void join(User user, Team team) {
        Participation participation = new Participation(user, team, false);
        team.getParticipants().add(participation);
        return;
    }

    @Transactional
    public void leaveTeam(Long userId, Team team) {
        ArrayList<Participation> participationList = new ArrayList<>();
        team.getParticipants().stream().filter(p -> p.getUser().getId().equals(userId))
                .forEach(participationList::add);
        participationList.forEach(p -> team.getParticipants().remove(p));
    }

    public Team findTeam(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(NoSuchElementException::new);
    }

    public Team findTeamByNotification(Notification notification) {
        return teamRepository.findByNotification(notification);
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
