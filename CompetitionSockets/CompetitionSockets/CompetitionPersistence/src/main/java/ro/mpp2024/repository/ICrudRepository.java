package ro.mpp2024.repository;

public interface ICrudRepository<Entity, Tid> {
    void add(Entity elem);
    void delete(Entity elem);
    void update(Entity elem, Tid id);
    Entity findById(Integer id);
    Iterable<Entity> findAll();
}
