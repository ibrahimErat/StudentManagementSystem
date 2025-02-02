import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManagerImpl implements StudentManager {
    private static final String DB_URL = DatabaseConnection.DB_URL;
    
    @Override
    public void addStudent(Student student) throws DatabaseException {
        DatabaseConnection.addStudent(student);
    }
    
    @Override
    public void deleteStudent(String studentId) throws DatabaseException {
        DatabaseConnection.deleteStudent(studentId);
    }
    
    @Override
    public void updateStudent(Student student) throws DatabaseException {
        DatabaseConnection.updateStudent(student);
    }
    
    @Override
    public Student getStudent(String studentId) throws DatabaseException {
        return DatabaseConnection.getStudent(studentId);
    }
    
    @Override
    public List<Student> getAllStudents() throws DatabaseException {
        return DatabaseConnection.getAllStudents();
    }
}
