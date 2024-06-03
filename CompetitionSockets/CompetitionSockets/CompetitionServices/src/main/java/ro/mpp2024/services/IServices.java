package ro.mpp2024.services;

import ro.mpp2024.model.*;

public interface IServices {
    void AddOperator(String username, String email, String password, String city) throws CompetitionException;
    Operator MatchByUserAndPassword(Operator operator, IObserver client) throws CompetitionException;
    Iterable<Group> FindAllGroups() throws CompetitionException;
    Group FindGroupByAge(int age) throws CompetitionException;
    Iterable<Contestant> FindAllByContest(Integer idContest);
    Iterable<ContestantServiceDTO> FindAllByContestWithParticipationCount(Integer idContest) throws CompetitionException;
    Iterable<Contestant> findAllByGroup(Integer idGroup);

    Iterable<Contest> FindAllByGroup(int idGroup) throws CompetitionException;
    Iterable<ContestServiceDTO> FindAllByGroupWithParticipationCount(int idGroup) throws CompetitionException;
    void AddContestant(String name, int age, String cnp, IObserver client);

    Contestant AddContestant(String name, int age, String cnp) throws CompetitionException;

    void AddParticipation(Contestant contestant, Contest contest) throws CompetitionException;
    void Logout(Operator operator, IObserver client) throws CompetitionException;
}
