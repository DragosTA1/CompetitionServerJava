package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.model.Contestant;
import ro.mpp2024.model.ContestantServiceDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ContestantRepository implements IContestantRepository {
    private Connection con;
    private final JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();
    
    public ContestantRepository(Properties props) {
        logger.info("Initializing ContestantRepository with properties: {} ",props);
        dbUtils = new JdbcUtils(props);
    }
    @Override
    public void add(Contestant elem) {
        con = dbUtils.getConnection();
        logger.traceEntry("saving contestant {} ",elem);
//        Connection con=dbUtils.getConnection();
        try(PreparedStatement insertStatement = con.prepareStatement("insert into Contestants(age,name,cnp) values (?,?,?)")){
            insertStatement.setInt(1,elem.getAge());
            insertStatement.setString(2,elem.getName());
            insertStatement.setString(3,elem.getCNP());
            int result=insertStatement.executeUpdate();
            logger.trace("Saved {} instances",result);

            // Retrieve the last inserted row's ID using last_insert_rowid() function
            try (Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()");
                if (rs.next()) {
                    int lastId = rs.getInt(1);
                    logger.debug("ID of the last inserted contestant: {}", lastId);
                    // You can use this ID as needed
                    elem.setID(lastId);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB "+e);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Contestant elem) {
        con = dbUtils.getConnection();
        logger.traceEntry("deleting contestant {} ",elem);
        try(PreparedStatement preStmt=con.prepareStatement("delete from Contestants where id=?")){
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
    public void update(Contestant elem, Integer id) {
        con = dbUtils.getConnection();
        logger.traceEntry("updating contestant with id {} to contest {} ",id, elem);
        try(PreparedStatement preStmt=con.prepareStatement("update Contestants set age=?,name=?,cnp=? where id=?")){
            preStmt.setInt(1,elem.getAge());
            preStmt.setString(2,elem.getName());
            preStmt.setString(3,elem.getCNP());
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
    public Contestant findById(Integer id) {
        con = dbUtils.getConnection();
        Contestant contestant = null;
        logger.traceEntry("finding contestant with id {}", id);
        try(PreparedStatement preStmt=con.prepareStatement("select * from Contestants where id=?")){
            preStmt.setInt(1,id);
            var result=preStmt.executeQuery();
            if (result.next()){
                String name = result.getString("name");
                int age = result.getInt("age");
                String CNP = result.getString("cnp");
                contestant = new Contestant(name, age, CNP);
                contestant.setID(id);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(contestant);
        return contestant;
    }

    @Override
    public Iterable<Contestant> findAllByContest(Integer idContest) {
        con = dbUtils.getConnection();
        logger.traceEntry("finding contestants by contest with id {}", idContest);
        List<Contestant> contestants = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Contestants where id in (select idContestant from Participations where idContest=?)")){
            preStmt.setInt(1, idContest);
            getContestantsByStatement(contestants, preStmt);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(contestants);
        return contestants;
    }
    @Override
    public Iterable<ContestantServiceDTO> findAllByContestWithParticipationCount(Integer idContest) {
        con = dbUtils.getConnection();
        logger.traceEntry("finding contestants by contest with id {}", idContest);
        List<ContestantServiceDTO> contestants = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("""
                select Contestants.*, count(Participations.id) as participationCount
                from Contestants
                         inner join Participations on Contestants.id = Participations.idContestant
                group by Contestants.id
                having Contestants.id in (select idContestant from Participations where idContest = ?)""")){
            preStmt.setInt(1, idContest);
            var result=preStmt.executeQuery();
            while (result.next()){
                int id = result.getInt("id");
                String name = result.getString("name");
                int age = result.getInt("age");
                String CNP = result.getString("cnp");
                int participationCount = result.getInt("participationCount");
                Contestant contestant = new Contestant(name, age, CNP);
                contestant.setID(id);
                contestants.add(new ContestantServiceDTO(contestant, participationCount));
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(contestants);
        return contestants;
    }

    @Override
    public Iterable<Contestant> findAllByGroup(Integer idGroup) {
        con = dbUtils.getConnection();
        logger.traceEntry("finding contestants by group with id {}", idGroup);
        List<Contestant> contestants = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Contestants where age between (select MinimumAge from Groups where id=?) and (select MaximumAge from Groups where id=?)")){
            preStmt.setInt(1, idGroup);
            preStmt.setInt(2, idGroup);
            getContestantsByStatement(contestants, preStmt);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(contestants);
        return contestants;
    }

    @Override
    public Iterable<Contestant> findAll() {
        con = dbUtils.getConnection();
        logger.traceEntry("finding all contestants");
        List<Contestant> contestants = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Contestants")){
            getContestantsByStatement(contestants, preStmt);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(contestants);
        return contestants;
    }

    private void getContestantsByStatement(List<Contestant> contestants, PreparedStatement preStmt) throws SQLException {
        con = dbUtils.getConnection();
        var result=preStmt.executeQuery();
        while (result.next()){
            int id = result.getInt("id");
            String name = result.getString("name");
            int age = result.getInt("age");
            String CNP = result.getString("cnp");
            Contestant contestant = new Contestant(name, age, CNP);
            contestant.setID(id);
            contestants.add(contestant);
        }
    }
}
