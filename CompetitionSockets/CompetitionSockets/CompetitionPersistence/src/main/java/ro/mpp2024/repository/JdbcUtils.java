package ro.mpp2024.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class JdbcUtils implements AutoCloseable{

    private final Properties jdbcProps;

    private static final Logger logger= LogManager.getLogger();

    public JdbcUtils(Properties props){
        jdbcProps=props;
    }

    private  Connection instance=null;

    private Connection getNewConnection(){
        logger.traceEntry();

        String url=jdbcProps.getProperty("jdbc.url");
        String user=jdbcProps.getProperty("jdbc.user");
        String pass=jdbcProps.getProperty("jdbc.pass");
        logger.info("trying to connect to database ... {}",url);
        logger.info("user: {}",user);
        logger.info("pass: {}", pass);
        Connection con=null;
        try {

            if (user!=null && pass!=null)
                con= DriverManager.getConnection(url,user,pass);
            else
                con=DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting connection "+e);
        }
        return con;
    }

    public Connection getConnection(){
        logger.traceEntry();
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(instance);
        return instance;
    }

    @Override
    public void close() throws Exception {
        logger.traceEntry();
        try {
            if (instance!=null && !instance.isClosed())
                instance.close();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error closing connection "+e);
        }
        logger.traceExit();
    }
}