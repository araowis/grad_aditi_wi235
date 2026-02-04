package persistence.operations;

import java.sql.*;

public class DatabaseConnector {
    public static Connection createConnection() {
      Connection conn = null;
      try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Fontaine", "postgres", "hydroarchon");
      } 
      catch (SQLException ex) {
            ex.printStackTrace();
      }
      return conn;
   }
}
