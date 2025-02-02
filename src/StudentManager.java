import java.util.List;

public interface StudentManager {
    void addStudent(Student student) throws DatabaseException;
    void deleteStudent(String studentId) throws DatabaseException;
    void updateStudent(Student student) throws DatabaseException;
    Student getStudent(String studentId) throws DatabaseException;
    List<Student> getAllStudents() throws DatabaseException;
}
