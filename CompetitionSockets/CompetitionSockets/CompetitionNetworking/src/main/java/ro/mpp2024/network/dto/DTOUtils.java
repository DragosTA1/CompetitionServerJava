package ro.mpp2024.network.dto;

import ro.mpp2024.model.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DTOUtils {
    public static Operator getFromDTO(OperatorDTO operatorDTO) {
        Operator op = new Operator(operatorDTO.getUsername(), operatorDTO.getEmail(), operatorDTO.getPassword(), operatorDTO.getCity());
        op.setID(operatorDTO.getId());
        return op;
    }
    public static OperatorDTO getDTO(Operator operator) {
        return new OperatorDTO(operator.getID(), operator.getUsername(), operator.getEmail(), operator.getPassword(), operator.getCity());
    }
    public static ArrayList<GroupDTO> getGroupDTOList(Iterable<Group> groups) {
        ArrayList<GroupDTO> groupDTOS = new ArrayList<>();
        for (Group group : groups) {
            groupDTOS.add(new GroupDTO(group.getID(), group.getMinimumAge(), group.getMaximumAge()));
        }
        return groupDTOS;
    }
    public static ArrayList<ContestDTO> getContestServiceDTOList(Iterable<ContestServiceDTO> contestServiceDTOS) {
        ArrayList<ContestDTO> contestDTOS = new ArrayList<>();
        for (ContestServiceDTO c : contestServiceDTOS) {
            ContestDTO contestDTO = new ContestDTO(c.getContest(), c.getParticipationCount());
            contestDTOS.add(contestDTO);
        }
        return contestDTOS;
    }
    public static ArrayList<ContestantDTO> getContestantServiceDTOList(Iterable<ContestantServiceDTO> contestantServiceDTOS) {
        ArrayList<ContestantDTO> contestantDTOS = new ArrayList<>();
        for (ContestantServiceDTO c : contestantServiceDTOS) {
            ContestantDTO contestantDTO = new ContestantDTO(c.getContestant(), c.getParticipationCount());
            contestantDTOS.add(contestantDTO);
        }
        return contestantDTOS;
    }
    public static Iterable<Group> getGroupListFromDTO(Iterable<GroupDTO> groupDTOS) {
        ArrayList<Group> groups = new ArrayList<>();
        for (GroupDTO groupDTO : groupDTOS) {
            Group group = new Group(groupDTO.getMinimumAge(), groupDTO.getMaximumAge());
            group.setID(groupDTO.getId());
            groups.add(group);
        }
        return groups;
    }

    public static Iterable<ContestServiceDTO> getContestServiceDTOListFromDTO(Iterable<ContestDTO> contestServiceDTOS) {
        ArrayList<ContestServiceDTO> contestServiceDTOList = new ArrayList<>();
        for (ContestDTO dto : contestServiceDTOS) {
            ContestServiceDTO contestServiceDTO = new ContestServiceDTO(dto.getContest(), dto.getParticipationCount());
            contestServiceDTOList.add(contestServiceDTO);
        }
        return contestServiceDTOList;
    }
    public static Iterable<Contest> getContestListFromDTO(Iterable<ContestDTO> contestDTOS) {
        ArrayList<Contest> contests = new ArrayList<>();
        for (ContestDTO contestDTO : contestDTOS) {
            Contest contest = new Contest(contestDTO.getType(), contestDTO.getContest().getParentGroup());
            contest.setID(contestDTO.getContest().getID());
            contests.add(contest);
        }
        return contests;
    }
    public static Iterable<ContestantServiceDTO> getContestantServiceDTOListFromDTO(Iterable<ContestantDTO> contestantDTOS) {
        ArrayList<ContestantServiceDTO> contestantServiceDTOList = new ArrayList<>();
        for (ContestantDTO dto : contestantDTOS) {
            ContestantServiceDTO contestantServiceDTO = new ContestantServiceDTO(dto.getContestant(), dto.getParticipationCount());
            contestantServiceDTOList.add(contestantServiceDTO);
        }
        return contestantServiceDTOList;
    }
    public static ContestantDTO getDTO(Contestant contestant) {
        return new ContestantDTO(contestant.getID(),contestant.getName(), contestant.getAge(), contestant.getCNP());
    }
    public static Contestant getFromDTO(ContestantDTO contestantDTO) {
        Contestant c = new Contestant(contestantDTO.getName(), contestantDTO.getAge(), contestantDTO.getCNP());
        c.setID(contestantDTO.getId());
        return c;
    }

    public static ArrayList<ContestDTO> getContestDTOList(Iterable<Contest> contests) {
        ArrayList<ContestDTO> contestDTOS = new ArrayList<>();
        for (Contest c : contests) {
            ContestDTO contestDTO = new ContestDTO(c, 0);
            contestDTOS.add(contestDTO);
        }
        return contestDTOS;
    }
    public static ParticipationDTO getDTO(Participation participation) {
        if(participation.getID() != null && participation.getDate() != null)
            return new ParticipationDTO(participation.getID(), participation.getContestant(), participation.getContest(), participation.getDate());
        return new ParticipationDTO(participation.getContestant(), participation.getContest());
    }

    public static Participation getFromDTO(ParticipationDTO participationDTO) {
        Date date;
        try {
            DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

            date = dateFormat.parse(participationDTO.getDateString());
        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
        }
        Participation p = new Participation(participationDTO.getContestant(), participationDTO.getContest(), date);
        p.setID(participationDTO.getId());
        return p;
    }
}