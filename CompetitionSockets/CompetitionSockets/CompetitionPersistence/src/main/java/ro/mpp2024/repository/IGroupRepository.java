package ro.mpp2024.repository;

import ro.mpp2024.model.Group;

import java.util.Collection;

public interface IGroupRepository extends ICrudRepository<Group, Integer> {
    Group findGroupByContest(int idContest);
    Group findGroupByAge(int age);
    Group findGroupByParticipation(int idParticipation);
    Collection<Group> getAll();
}
