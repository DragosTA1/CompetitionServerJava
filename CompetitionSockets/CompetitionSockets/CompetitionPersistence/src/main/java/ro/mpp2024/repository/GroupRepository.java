package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.model.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class GroupRepository implements IGroupRepository {
    private static final Logger logger= LogManager.getLogger();
    private final JdbcUtils dbUtils;
    private Connection con;

    public GroupRepository(Properties props) {
        logger.info("Initializing GroupRepository with properties: {} ",props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Group elem) {

    }

    @Override
    public void delete(Group elem) {

    }

    @Override
    public void update(Group elem, Integer id) {

    }

    @Override
    public Group findById(Integer id) {
        return null;
    }

    @Override
    public Group findGroupByContest(int idContest) {
        con = dbUtils.getConnection();
        logger.traceEntry("finding group with contest id {}", idContest);
        Group group = null;
        try(PreparedStatement preStmt = con.prepareStatement("select Groups.* from Groups inner join Contests on Groups.id = Contests.idParentGroup where Contests.id = ?")) {
            preStmt.setInt(1, idContest);
            group = getGroup(group, preStmt);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(group);
        return group;
    }

    @Override
    public Group findGroupByAge(int age) {
        con = dbUtils.getConnection();
        logger.traceEntry("finding group with age {}", age);
        Group group = null;
        try(PreparedStatement preStmt = con.prepareStatement("select * from Groups where minimumAge <= ? and maximumAge >= ?")) {
            preStmt.setInt(1, age);
            preStmt.setInt(2, age);
            group = getGroup(group, preStmt);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(group);
        return group;
    }

    private Group getGroup(Group group, PreparedStatement preStmt) throws SQLException {
        con = dbUtils.getConnection();
        var result = preStmt.executeQuery();
        if(result.next()) {
            int id = result.getInt("id");
            int minimumAge = result.getInt("minimumAge");
            int maximumAge = result.getInt("maximumAge");
            group = new Group(minimumAge, maximumAge);
            group.setID(id);
        }
        return group;
    }

    @Override
    public Group findGroupByParticipation(int idParticipation) {
        con = dbUtils.getConnection();
        logger.traceEntry("finding group with participation id {}", idParticipation);
        Group group = null;
        try(PreparedStatement preStmt = con.prepareStatement("select Groups.* from Groups inner join Contests on Groups.id = Contests.idParentGroup inner join Participations on Contests.id = Participations.idContest where Participations.id = ?")) {
            preStmt.setInt(1, idParticipation);
            group = getGroup(group, preStmt);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(group);
        return group;
    }

    @Override
    public Iterable<Group> findAll() {
        con = dbUtils.getConnection();
        logger.traceEntry("finding all groups");
        List<Group> groups = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from Groups")) {
            var result = preStmt.executeQuery();
            while(result.next()) {
                int id = result.getInt("id");
                int minimumAge = result.getInt("minimumAge");
                int maximumAge = result.getInt("maximumAge");
                Group group = new Group(minimumAge, maximumAge);
                group.setID(id);
                groups.add(group);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(groups);
        return groups;
    }

    @Override
    public Collection<Group> getAll() {
        con = dbUtils.getConnection();
        logger.traceEntry("getting all groups");
        Collection<Group> groups = new ArrayList<>();

        try(PreparedStatement preStmt = con.prepareStatement("select * from Groups")) {
            var result = preStmt.executeQuery();
            while(result.next()) {
                int id = result.getInt("id");
                int minimumAge = result.getInt("minimumAge");
                int maximumAge = result.getInt("maximumAge");
                Group group = new Group(minimumAge, maximumAge);
                group.setID(id);
                groups.add(group);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(groups);
        return groups;
    }
}
