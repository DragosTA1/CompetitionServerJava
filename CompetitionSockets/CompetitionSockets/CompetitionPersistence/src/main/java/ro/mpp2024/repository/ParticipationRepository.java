package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.model.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParticipationRepository implements IParticipationRepository {
    private static final Logger logger= LogManager.getLogger();
    private final JdbcUtils dbUtils;
    private Connection con;

    public ParticipationRepository(Properties props) {
        logger.info("Initializing ParticipationRepository with properties: {} ",props);
        dbUtils = new JdbcUtils(props);
    }
    @Override
    public void delete(Participation elem) {
        con = dbUtils.getConnection();
        logger.traceEntry("deleting participation {} ",elem);
        try(PreparedStatement preStmt=con.prepareStatement("delete from Participations where id=?")){
            preStmt.setInt(1,elem.getID());
            int result=preStmt.executeUpdate();
            logger.trace("Deleted {} instances",result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB "+e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Participation elem, Integer id) {
        con = dbUtils.getConnection();
        logger.traceEntry("updating participation with id {} to participation {} ",id,elem);
        try(PreparedStatement preStmt=con.prepareStatement("update Participations set idContestant=?, idContest=?, date=? where id=?")){
            preStmt.setInt(1,elem.getContestant().getID());
            preStmt.setInt(2,elem.getContest().getID());
            preStmt.setDate(3, new Date(elem.getDate().getTime()));
            preStmt.setInt(4,id);
            int result=preStmt.executeUpdate();
            logger.trace("Updated {} instances",result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB "+e);
        }
        logger.traceExit();
    }

    @Override
    public Participation findById(Integer id) {
        con = dbUtils.getConnection();
        logger.traceEntry("finding participation with id {} ",id);
        Participation participation = null;
        try(PreparedStatement preStmt=
                        con.prepareStatement("select Participations.*, Contestants.age, Contestants.name, Contestants.CNP, Contests.type, Contests.idParentGroup, Groups.minimumAge, Groups.maximumAge from Participations inner join Contestants on Participations.idContestant = Contestants.id inner join Contests on Participations.idContest = Contests.id inner join Groups on Groups.id = Contests.idParentGroup where Participations.id=?")) {
            preStmt.setInt(1, id);
            try (var result = preStmt.executeQuery()) {
                if (result.next()) {
                    int idContestant = result.getInt("idContestant");
                    int idContest = result.getInt("idContest");
                    Date date = result.getDate("date");

                    Contestant contestant = new Contestant(result.getString("name"), result.getInt("age"), result.getString("CNP"));
                    contestant.setID(idContestant);

                    Group group = new Group(result.getInt("minimumAge"), result.getInt("maximumAge"));
                    group.setID(result.getInt("idParentGroup"));

                    Contest contest = new Contest(result.getInt("type"), group);
                    contest.setID(idContest);

                    participation = new Participation(contestant, contest, date);
                    participation.setID(id);

                }
            } catch (SQLException e) {
                logger.error(e);
                System.err.println("Error DB " + e);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(participation);
        return participation;
    }

    @Override
    public Iterable<Participation> findAll() {
        return null;
    }

    @Override
    public void add(Participation elem) {
        con = dbUtils.getConnection();
        logger.traceEntry("saving participation {}", elem);
        try (PreparedStatement preparedStatement = con.prepareStatement("insert into Participations(idContestant, idContest, date) values (?, ?, ?)")) {
            preparedStatement.setInt(1, elem.getContestant().getID());
            preparedStatement.setInt(2, elem.getContest().getID());
            preparedStatement.setDate(3, new Date(elem.getDate().getTime()));
            int result = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public Iterable<Participation> findAllByContestant(Integer id) {
        con = dbUtils.getConnection();
        logger.traceEntry("finding all participations for contestant with id {}", id);
        List<Participation> participations = new ArrayList<Participation>();
        try(PreparedStatement preparedStatement = con.prepareStatement("select Participations.*, Contestants.age, Contestants.name, Contestants.CNP, Contests.type, Contests.idParentGroup, Groups.minimumAge, Groups.maximumAge from Participations inner join Contestants on Participations.idContestant = Contestants.id inner join Contests on Participations.idContest = Contests.id inner join Groups on Groups.id = Contests.idParentGroup where Participations.idContestant=?")) {
            preparedStatement.setInt(1, id);
            try (var result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    int idContestant = result.getInt("idContestant");
                    int idContest = result.getInt("idContest");
                    Date date = result.getDate("date");

                    Contestant contestant = new Contestant(result.getString("name"), result.getInt("age"), result.getString("CNP"));
                    contestant.setID(idContestant);

                    Group group = new Group(result.getInt("minimumAge"), result.getInt("maximumAge"));
                    group.setID(result.getInt("idParentGroup"));

                    Contest contest = new Contest(result.getInt("type"), group);
                    contest.setID(idContest);

                    var participation = new Participation(contestant, contest, date);
                    participation.setID(id);
                    participations.add(participation);
                }
            } catch (SQLException e) {
                logger.error(e);
                System.err.println("Error DB " + e);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(participations);
        return participations;
    }

    @Override
    public int countByContest(Integer id) {
        con = dbUtils.getConnection();
        logger.traceEntry("counting participations for contest with id {}", id);
        int count = 0;
        try(PreparedStatement preparedStatement = con.prepareStatement("select count(*) as count from Participations where idContest=?")) {
            preparedStatement.setInt(1, id);
            try (var result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    count = result.getInt("count");
                }
            } catch (SQLException e) {
                logger.error(e);
                System.err.println("Error DB " + e);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(count);
        return count;
    }
}
