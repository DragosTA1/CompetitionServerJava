package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.model.Contest;
import ro.mpp2024.model.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class ContestRepository implements IContestRepository {
    private static final Logger logger= LogManager.getLogger();
    private final JdbcUtils dbUtils;
    private Connection con;

    public ContestRepository(Properties props) {
        logger.info("Initializing ContestRepository with properties : {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Iterable<Contest> findAllByContestant(int idContestant) {
        con = dbUtils.getConnection();
        logger.traceEntry("finding all contests for contestant with id {}", idContestant);
        List<Contest> contests = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select Contests.*, Groups.minimumAge, Groups.maximumAge from Contests inner join Participations on Contests.id = Participations.idContest inner join Groups on Groups.id = Contests.idParentGroup where Participations.idContestant = ?")) {
            preStmt.setInt(1, idContestant);
            var result = preStmt.executeQuery();
            while(result.next()) {
                int id = result.getInt("id");
                int type = result.getInt("type");
                int idParentGroup = result.getInt("idParentGroup");

                Group group = new Group(result.getInt("minimumAge"), result.getInt("maximumAge"));
                group.setID(idParentGroup);
                Contest contest = new Contest(type, group);
                contest.setID(id);
                contests.add(contest);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(contests);
        return contests;
    }

    @Override
    public Iterable<Contest> findAllByGroup(int idGroup) {
        con = dbUtils.getConnection();
        logger.traceEntry("finding all contests for group with id {}", idGroup);
        List<Contest> contests = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select Contests.*, Groups.minimumAge, Groups.maximumAge from Contests inner join Groups on Contests.idParentGroup = Groups.id where idParentGroup = ?")) {
            preStmt.setInt(1, idGroup);
            var result = preStmt.executeQuery();
            while(result.next()) {
                int id = result.getInt("id");
                int type = result.getInt("type");

                Group group = new Group(result.getInt("minimumAge"), result.getInt("maximumAge"));
                group.setID(result.getInt("idParentGroup"));

                Contest contest = new Contest(type, group);
                contest.setID(id);
                contests.add(contest);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(contests);
        return contests;
    }

    @Override
    public void add(Contest elem) {
        con = dbUtils.getConnection();
        logger.traceEntry("saving contest {}", elem);
        try(PreparedStatement insertStatement = con.prepareStatement("insert into Contests(type, idParentGroup) values (?, ?)")){
            insertStatement.setInt(1,elem.getType());
            insertStatement.setInt(2,elem.getParentGroup().getID());
            int result=insertStatement.executeUpdate();
            logger.trace("Saved {} instances",result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB "+e);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Contest elem) {

    }

    @Override
    public void update(Contest elem, Integer id) {

    }

    @Override
    public Contest findById(Integer id) {
        return null;
    }

    @Override
    public Iterable<Contest> findAll() {
        con = dbUtils.getConnection();
        logger.traceEntry("finding all contests");
        List<Contest> contests = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select Contests.*, Groups.minimumAge, Groups.maximumAge from Contests inner join Groups on Contests.idParentGroup = Groups.id")) {
            var result = preStmt.executeQuery();
            while(result.next()) {
                int id = result.getInt("id");
                int type = result.getInt("type");
                int idParentGroup = result.getInt("idParentGroup");

                Group group = new Group(result.getInt("minimumAge"), result.getInt("maximumAge"));
                group.setID(idParentGroup);
                Contest contest = new Contest(type, group);
                contest.setID(id);
                contests.add(contest);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(contests);
        return contests;
    }

    @Override
    public Collection<Contest> getAll() {
        con = dbUtils.getConnection();
        logger.traceEntry("getting all contests");
        Collection<Contest> contests = new ArrayList<>();

        try(PreparedStatement preStmt = con.prepareStatement("select Contests.*, Groups.minimumAge, Groups.maximumAge from Contests inner join Groups on Contests.idParentGroup = Groups.id")) {
            var result = preStmt.executeQuery();
            while(result.next()) {
                int id = result.getInt("id");
                int type = result.getInt("type");
                int idParentGroup = result.getInt("idParentGroup");

                Group group = new Group(result.getInt("minimumAge"), result.getInt("maximumAge"));
                group.setID(idParentGroup);
                Contest contest = new Contest(type, group);
                contest.setID(id);
                contests.add(contest);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(contests);
        return contests;
    }
}
