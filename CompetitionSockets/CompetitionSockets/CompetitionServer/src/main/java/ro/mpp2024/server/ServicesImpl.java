package ro.mpp2024.server;

import ro.mpp2024.model.ContestServiceDTO;
import ro.mpp2024.services.CompetitionException;
import ro.mpp2024.services.IObserver;
import ro.mpp2024.services.IServices;
import ro.mpp2024.model.*;
import ro.mpp2024.repository.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImpl implements  IServices{
    private final IOperatorRepository _operatorRepository;
    private final IContestantRepository _contestantRepository;
    private final IContestRepository _contestRepository;
    private final IGroupRepository _groupRepository;
    private final IParticipationRepository _participationRepository;
    private final Map<Integer, IObserver> loggedOperators;

    public ServicesImpl(IOperatorRepository operatorRepository, IContestantRepository contestantRepository, IContestRepository contestRepository, IGroupRepository groupRepository, IParticipationRepository participationRepository) {
        this._operatorRepository = operatorRepository;
        this._contestantRepository = contestantRepository;
        this._contestRepository = contestRepository;
        this._groupRepository = groupRepository;
        this._participationRepository = participationRepository;

        loggedOperators = new java.util.concurrent.ConcurrentHashMap<>();
    }

    @Override
    public void AddOperator(String username, String email, String password, String city) {
        Operator op = new Operator(username, email, password, city);
        _operatorRepository.add(op);
    }

    @Override
    public Operator MatchByUserAndPassword(Operator operator, IObserver client) throws CompetitionException {
        var op = _operatorRepository.matchByUserAndPassword(operator.getUsername(), operator.getPassword());
        if(op != null) {
            if(loggedOperators.get(op.getID()) != null)
                throw new CompetitionException("Operator already logged in.");
            loggedOperators.put(op.getID(), client);

            System.out.println(loggedOperators.size() + " operators logged in.");
            System.out.println(loggedOperators.toString());
        }
        return op;
    }

    @Override
    public Iterable<Group> FindAllGroups() {
        return _groupRepository.findAll();
    }

    @Override
    public Group FindGroupByAge(int age) {
        return _groupRepository.findGroupByAge(age);
    }

    @Override
    public Iterable<Contestant> FindAllByContest(Integer idContest) {
        return _contestantRepository.findAllByContest(idContest);
    }

    @Override
    public Iterable<ContestantServiceDTO> FindAllByContestWithParticipationCount(Integer idContest) {
        return _contestantRepository.findAllByContestWithParticipationCount(idContest);
    }

    @Override
    public Iterable<Contestant> findAllByGroup(Integer idGroup) {
        return _contestantRepository.findAllByGroup(idGroup);
    }

    @Override
    public Iterable<Contest> FindAllByGroup(int idGroup) {
        return _contestRepository.findAllByGroup(idGroup);
    }

    @Override
    public Iterable<ContestServiceDTO> FindAllByGroupWithParticipationCount(int idGroup) {
        ArrayList<ContestServiceDTO> result = new ArrayList<>();
        var lst = _contestRepository.findAllByGroup(idGroup);
        for (Contest c : lst) {
            ContestServiceDTO dto = new ContestServiceDTO(c, _participationRepository.countByContest(c.getID()));
            result.add(dto);
        }
        return result;
    }

    @Override
    public void AddContestant(String name, int age, String cnp, IObserver client) {
        Contestant contestant = new Contestant(name, age, cnp);
        _contestantRepository.add(contestant);
    }

    @Override
    public Contestant AddContestant(String name, int age, String cnp) {
        System.out.println("Adding contestant: " + name + " " + age + " " + cnp);
        Contestant contestant = new Contestant(name, age, cnp);
        _contestantRepository.add(contestant);
        System.out.println("Added contestant: " + contestant);
        return contestant;
    }
    @Override
    public void AddParticipation(Contestant contestant, Contest contest)
    {
        Participation participation = new Participation(contestant, contest, new Date());
        _participationRepository.add(participation);
        notifyOperatorsLoggedIn(participation);
    }

    private void notifyOperatorsLoggedIn(Participation participation) {
        ExecutorService executor= Executors.newFixedThreadPool(5);

        for (IObserver operatorClient : loggedOperators.values()) {
            if(operatorClient != null) {
                    executor.execute(() -> {
                    try {
                        operatorClient.participationAdded(participation);
                    } catch (CompetitionException e) {
                        System.err.println("Error notifying operator " + e);
                    }
                });
            }
        }
        executor.shutdown();
    }

    @Override
    public void Logout(Operator operator, IObserver client) throws CompetitionException {
        IObserver localClient = loggedOperators.remove(operator.getID());
        if (localClient == null)
            throw new CompetitionException("User " + operator.getUsername() + " is not logged in.");
        System.out.println(loggedOperators.size() + " operators logged in.");
    }

//    public boolean validateCNP(String cnp) {
//        // validate id number using regex
//        // Regular expression pattern for CNP validation
//        String pattern = "^[1-9]\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])(0[1-9]|[1-4]\\d|5[0-2]|99)(00[1-9]|0[1-9]\\d|[1-9]\\d\\d)\\d$";
//
//        // Create a Pattern object with the pattern
//        Pattern regex = Pattern.compile(pattern);
//
//        // Check if the provided CNP matches the pattern
//        // Additional validation can be added here if needed
//        // CNP is not valid
//        return regex.matcher(cnp).matches(); // CNP is valid
//    }
}
