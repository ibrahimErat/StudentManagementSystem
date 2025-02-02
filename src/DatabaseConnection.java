import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class DatabaseConnection {
    public static final String DB_URL = "jdbc:sqlite:" + new File("database/student_database.db").getAbsolutePath();
    
    static {
        try {
            // Veritabanı klasörünü oluştur
            new File("database").mkdirs();
            
            // SQLite JDBC sürücüsünü yükle
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC sürücüsü başarıyla yüklendi.");
            
            // Veritabanı bağlantısını test et
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                System.out.println("Veritabanı bağlantısı başarılı: " + DB_URL);
                createTable();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC sürücüsü bulunamadı: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Veritabanı bağlantısı kurulamadı: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS students (
                student_id TEXT PRIMARY KEY,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                age INTEGER NOT NULL,
                gpa REAL NOT NULL
            )
        """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    public static void addStudent(Student student) throws DatabaseException {
        String sql = "INSERT INTO students (student_id, first_name, last_name, age, gpa) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getStudentID());
            pstmt.setString(2, student.getFirstName());
            pstmt.setString(3, student.getLastName());
            pstmt.setInt(4, student.getAge());
            pstmt.setDouble(5, student.getGpa());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DatabaseException("Bu ID'ye sahip öğrenci zaten mevcut: " + student.getStudentID());
            }
            throw new DatabaseException("Öğrenci eklenirken hata oluştu: " + e.getMessage());
        }
    }
    
    public static void updateStudent(Student student) throws DatabaseException {
        String sql = "UPDATE students SET first_name = ?, last_name = ?, age = ?, gpa = ? WHERE student_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setInt(3, student.getAge());
            pstmt.setDouble(4, student.getGpa());
            pstmt.setString(5, student.getStudentID());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Güncellenecek öğrenci bulunamadı: " + student.getStudentID());
            }
        } catch (SQLException e) {
            throw new DatabaseException("Öğrenci güncellenirken hata oluştu: " + e.getMessage());
        }
    }
    
    public static void deleteStudent(String studentId) throws DatabaseException {
        String sql = "DELETE FROM students WHERE student_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentId);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Silinecek öğrenci bulunamadı: " + studentId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Öğrenci silinirken hata oluştu: " + e.getMessage());
        }
    }
    
    public static Student getStudent(String studentId) throws DatabaseException {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                        rs.getString("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getDouble("gpa")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Öğrenci aranırken hata oluştu: " + e.getMessage());
        }
    }
    
    public static List<Student> getAllStudents() throws DatabaseException {
        String sql = "SELECT * FROM students ORDER BY student_id";
        List<Student> students = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(new Student(
                    rs.getString("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getDouble("gpa")
                ));
            }
            return students;
        } catch (SQLException e) {
            throw new DatabaseException("Öğrenciler listelenirken hata oluştu: " + e.getMessage());
        }
    }
} 