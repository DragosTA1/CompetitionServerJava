package ro.mpp2024.repository;

import ro.mpp2024.model.Participation;

public interface IParticipationRepository extends ICrudRepository<Participation, Integer> {
    Iterable<Participation> findAllByContestant(Integer id);

    int countByContest(Integer id);
}
