package ro.mpp2024.repository;

import ro.mpp2024.model.Contest;

import java.util.Collection;

public interface IContestRepository extends ICrudRepository<Contest, Integer> {
    Iterable<Contest> findAllByContestant(int idContestant);
    Iterable<Contest> findAllByGroup(int idGroup);
    Collection<Contest> getAll();
}
