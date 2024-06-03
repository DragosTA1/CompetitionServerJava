package ro.mpp2024.UI.Controllers;


import ro.mpp2024.model.Operator;
import ro.mpp2024.model.Participation;
import ro.mpp2024.repository.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.Properties;

public class CompetitionMain {
    public static void main(String[] args) {
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        testOperatorRepository(props);
        testGroupRepository(props);
        testContestantRepository(props);
        testParticipationRepository(props);
        testContestRepository(props);
    }

    private static void testOperatorRepository(Properties props) {
        IOperatorRepository operatorRepository = new OperatorRepository(props);
        operatorRepository.add(new Operator("username","email", "password", "city"));
        var operator = operatorRepository.findAll().iterator().next();
        operatorRepository.update(new Operator("new_username", "email", "password", "city"), operator.getID());
        operator = operatorRepository.matchByUserAndPassword("new_username","password");
        operatorRepository.delete(operator);
        System.out.println(operatorRepository.findAll());
    }

    private static void testGroupRepository(Properties props) {
        IGroupRepository groupRepository = new GroupRepository(props);
        var group = groupRepository.findGroupByAge(6);
        groupRepository.getAll();
        groupRepository.findAll();
        IContestRepository contestRepository = new ContestRepository(props);
        groupRepository.findGroupByContest(contestRepository.getAll().iterator().next().getID());
        IParticipationRepository participationRepository = new ParticipationRepository(props);
        groupRepository.findGroupByParticipation(participationRepository.findById(1).getID());
    }

    private static void testContestantRepository(Properties props) {
        IContestantRepository contestantRepository = new ContestantRepository(props);
//        contestantRepository.add(new Contestant("name", 6, "5030919021592"));
        contestantRepository.findAll();
        IGroupRepository groupRepository = new GroupRepository(props);
        contestantRepository.findAllByGroup(groupRepository.findGroupByAge(6).getID());
        IContestRepository contestRepository = new ContestRepository(props);
        contestantRepository.findAllByContest(contestRepository.findAll().iterator().next().getID());
    }

    private static void testParticipationRepository(Properties props) {
        IParticipationRepository participationRepository = new ParticipationRepository(props);
        IContestantRepository contestantRepository = new ContestantRepository(props);
        IContestRepository contestRepository = new ContestRepository(props);
        participationRepository.add(new Participation(contestantRepository.findAll().iterator().next(), contestRepository.findAll().iterator().next(), Date.valueOf("2021-05-05")));
        participationRepository.findAllByContestant(contestantRepository.findAll().iterator().next().getID());
    }

    private static void testContestRepository(Properties props) {
        IContestRepository contestRepository = new ContestRepository(props);
        contestRepository.findAll();
        contestRepository.getAll();
        IContestantRepository contestantRepository = new ContestantRepository(props);
        IGroupRepository groupRepository = new GroupRepository(props);
        contestRepository.findAllByContestant(contestantRepository.findAll().iterator().next().getID());
        contestRepository.findAllByGroup(groupRepository.findAll().iterator().next().getID());
    }
}