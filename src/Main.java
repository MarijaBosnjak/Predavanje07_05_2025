import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
DataSource dataSource=createDataSource();
try(Connection connection=dataSource.getConnection()){
    System.out.println("Uspješno ste spojeni na bazu podataka");
    //try-catch blok za izvršavanje transakcija
    try(Statement stmt1=connection.createStatement(); Statement stmt2=connection.createStatement()){
        connection.setAutoCommit(false);//iskljucivanje automatskog komita transakcije
        stmt1.executeUpdate("INSERT INTO Drzava (Naziv) VALUES ('Nigerija')");
        stmt2.executeUpdate("UPDATE Drzava SET Naziv='Germany' WHERE IDDRzava=2");
        connection.commit();
        System.out.println("Transakcija izvršena");
    }
    catch(SQLException e){
        connection.rollback();
        System.err.println("Transakcija ponistena");
    }
    Statement stmt=connection.createStatement();
    //Dohvacanje svih drzava
    ResultSet rs=stmt.executeQuery("SELECT IDDrzava, Naziv from Drzava");
    while(rs.next()){

    }
}
catch(SQLException e){
    System.err.println("Greška prilikom spajanja na bazu");
    e.printStackTrace();
}
    }
    private  static DataSource createDataSource(){
        SQLServerDataSource ds=new SQLServerDataSource();
        ds.setServerName("localHost");
        ds.setDatabaseName("AdventureWorksOBP");
        ds.setPortNumber(1433);
        ds.setUser("sa");
        ds.setPassword("SQL");
        ds.setEncrypt(false);
        return ds;
    }
}