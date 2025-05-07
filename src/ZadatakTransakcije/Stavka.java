package ZadatakTransakcije;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class Stavka {
    public static void main(String[] args) {
        DataSource dataSource = createDataSource();

        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Uspješno smo spojeni na bazu podataka");

            // Početak transakcije
            try (
                    Statement stmt1 = connection.createStatement();
                    Statement stmt2 = connection.createStatement()
            ) {
                connection.setAutoCommit(false);

                // Ažuriraj cijenu stavke s ID 8 (+10)
                stmt1.executeUpdate("UPDATE Stavka SET CijenaPoKomadu = CijenaPoKomadu + 10 WHERE IDStavka = 8");

                // Ažuriraj cijenu stavke s ID 9 (-10)
                stmt2.executeUpdate("UPDATE Stavka SET CijenaPoKomadu = CijenaPoKomadu - 10 WHERE IDStavka = 9");

                // Ako su obje uspješne, potvrdi transakciju
                connection.commit();
                System.out.println("Transakcija uspješno izvršena.");

            } catch (SQLException e) {
                // Ako dođe do greške, poništi sve
                connection.rollback();
                System.err.println("Greška tijekom transakcije. Transakcija poništena.");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println("Greška prilikom spajanja na bazu podataka.");
            e.printStackTrace();
        }
    }

    private static DataSource createDataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("localhost");
        ds.setDatabaseName("AdventureWorksOBP");
        ds.setPortNumber(1433);
        ds.setUser("sa");
        ds.setPassword("SQL");
        ds.setEncrypt(false);
        return ds;
    }
}
//TRANSAKCIJA ZA SQL
/*BEGIN TRANSACTION;

BEGIN TRY
UPDATE Stavka
SET CijenaPoKomadu = CijenaPoKomadu + 10
WHERE IDStavka = 8;

UPDATE Stavka
SET CijenaPoKomadu = CijenaPoKomadu - 10
WHERE IDStavka = 9;

COMMIT TRANSACTION;
PRINT 'Transakcija uspješno izvršena.';
END TRY
BEGIN CATCH
ROLLBACK TRANSACTION;
PRINT 'Došlo je do greške. Transakcija ponistena.';
PRINT ERROR_MESSAGE();
END CATCH;*/