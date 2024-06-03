package ro.mpp2024.repository;

import ro.mpp2024.model.Contestant;
import ro.mpp2024.model.ContestantServiceDTO;

public interface IContestantRepository extends ICrudRepository<Contestant, Integer> {
    Iterable<Contestant> findAllByContest(Integer idContest);
    Iterable<ContestantServiceDTO> findAllByContestWithParticipationCount(Integer idContest);
    Iterable<Contestant> findAllByGroup(Integer idGroup);
}
