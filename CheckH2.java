import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckH2 {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:h2:file:./backend/data/openlibdb;AUTO_SERVER=TRUE";
        String user = "sa";
        String password = "";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT isbn, titulo, id_vendedor FROM contenidos_digitales")) {
            
            System.out.println("Contenidos Digitales:");
            while (rs.next()) {
                System.out.println("ISBN: " + rs.getString("isbn") + ", Titulo: " + rs.getString("titulo") + ", Vendedor: " + rs.getString("id_vendedor"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
