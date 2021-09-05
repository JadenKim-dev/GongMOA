package GongMoa.gongmoa.service;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.Notification;
import GongMoa.gongmoa.domain.Participation;
import GongMoa.gongmoa.domain.Registration;
import GongMoa.gongmoa.domain.Team;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService {
    public boolean authorizeUserIsWriter(User user, Notification notification) {
        List<Registration> registrations = user.getRegistrations();
        if(registrations.isEmpty()){
            return false;
        }
        Registration findRegistration = Registration.findRegistrationFromListByNotification(registrations, notification);
//        log.info("findRegistration!=null : {}", findRegistration!=null);
//        log.info("findRegistration.isWriter() : {}", findRegistration!=null && findRegistration.isWriter());
        return findRegistration!=null && findRegistration.isWriter();
    }

    public boolean authorizeUserIsLeader(User user, Team team) {
        List<Participation> participants = team.getParticipants();
        if(participants.isEmpty()) {
            return false;
        }
        Participation findParticipation = Participation.findParticipationFromListByUser(participants, user);
        return findParticipation != null && findParticipation.isLeader();
    }

    public boolean authorizeUserIsTeamParticipant(User user, Team team) {
        List<Participation> participants = team.getParticipants();
        if(participants.isEmpty()) {
            return false;
        }
        Participation findParticipant = Participation.findParticipationFromListByUser(participants, user);
        return findParticipant!=null;
    }

}
