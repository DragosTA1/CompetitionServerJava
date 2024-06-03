package ro.mpp2024.repository;

import ro.mpp2024.model.Operator;

public interface IOperatorRepository extends ICrudRepository<Operator, Integer> {
    Operator matchByUserAndPassword(String user, String password);
}
