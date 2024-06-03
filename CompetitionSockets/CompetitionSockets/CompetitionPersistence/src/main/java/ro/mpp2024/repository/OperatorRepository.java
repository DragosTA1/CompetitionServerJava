package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.model.Operator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OperatorRepository implements IOperatorRepository {
    private static final Logger logger= LogManager.getLogger();
    private Connection con;
    private final JdbcUtils dbUtils;

    public OperatorRepository(Properties props) {
        logger.info("Initializing OperatorRepository with properties: {} ",props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Iterable<Operator> findAll() {
        con = dbUtils.getConnection();
        logger.traceEntry("finding all operators");
        List<Operator> operators = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from Operators")) {
            var result = preStmt.executeQuery();
            while(result.next()) {
                int id = result.getInt("id");
                String username = result.getString("username");
                String password = result.getString("password");
                String email = result.getString("email");
                String city = result.getString("city");

                Operator operator = new Operator(username, password, email, city);
                operator.setID(id);
                operators.add(operator);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(operators);
        return operators;
    }

    @Override
    public void add(Operator elem) {
        con = dbUtils.getConnection();
        logger.traceEntry("saving operator {}", elem);
        try(PreparedStatement insertStatement = con.prepareStatement("insert into Operators(username, password, email, city) values (?, ?, ?, ?)")){
            insertStatement.setString(1,elem.getUsername());
            insertStatement.setString(2,elem.getPassword());
            insertStatement.setString(3,elem.getEmail());
            insertStatement.setString(4,elem.getCity());
            int result=insertStatement.executeUpdate();
            logger.trace("Saved {} instances",result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB "+e);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Operator elem) {
        con = dbUtils.getConnection();
        logger.traceEntry("deleting operator {} ", elem);
        try(PreparedStatement preStmt = con.prepareStatement("delete from Operators where id=?")) {
            preStmt.setInt(1, elem.getID());
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Operator elem, Integer id) {
        con = dbUtils.getConnection();
        logger.traceEntry("updating operator with id {} to operator {}", id, elem);
        try(PreparedStatement preStmt = con.prepareStatement("update Operators set username=?, password=?, email=?, city=? where id=?")) {
            preStmt.setString(1, elem.getUsername());
            preStmt.setString(2, elem.getPassword());
            preStmt.setString(3, elem.getEmail());
            preStmt.setString(4, elem.getCity());
            preStmt.setInt(5, id);
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public Operator findById(Integer id) {
        con = dbUtils.getConnection();
        Operator operator = null;
        logger.traceEntry("finding operator with id {}", id);
        try(PreparedStatement preStmt = con.prepareStatement("select * from Operators where id=?")) {
            preStmt.setInt(1, id);
            var result = preStmt.executeQuery();
            if(result.next()) {
                String username = result.getString("username");
                String password = result.getString("password");
                String email = result.getString("email");
                String city = result.getString("city");

                operator = new Operator(username, password, email, city);
                operator.setID(id);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(operator);
        return operator;
    }

    @Override
    public Operator matchByUserAndPassword(String user, String password) {
        con = dbUtils.getConnection();
        Operator operator = null;
        logger.traceEntry("finding operator with password {}", password);
        try(PreparedStatement preStmt = con.prepareStatement("select * from Operators where username=? and password=?")) {
            preStmt.setString(1, user);
            preStmt.setString(2, password);
            var result = preStmt.executeQuery();
            if(result.next()) {
                String username = result.getString("username");
                String email = result.getString("email");
                String city = result.getString("city");
                int id = result.getInt("id");
                operator = new Operator(username, password, email, city);
                operator.setID(id);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(operator);
        return operator;
    }
}
