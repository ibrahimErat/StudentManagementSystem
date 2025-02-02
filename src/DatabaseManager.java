import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:students.db";
    private static DatabaseManager instance;
    
    private DatabaseManager() {
        // Singleton constructor
    }
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    
    // Bağlantıyı test etmek için yardımcı metod
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return true;
        } catch (SQLException e) {
            System.err.println("Bağlantı hatası: " + e.getMessage());
            return false;
        }
    }
    
    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Bağlantı kapatılırken hata: " + e.getMessage());
            }
        }
    }
    
    public void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Statement kapatılırken hata: " + e.getMessage());
            }
        }
    }
    
    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("ResultSet kapatılırken hata: " + e.getMessage());
            }
        }
    }
} 