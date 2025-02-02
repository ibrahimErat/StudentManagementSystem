import java.util.List;

public class DatabaseTest {
    public static void main(String[] args) {
        StudentManagerImpl studentManager = new StudentManagerImpl();
        
        try {
            // Test verisi oluştur
            Student newStudent = new Student(
                "TST001",
                "Test",
                "Öğrenci",
                20,
                85.5
            );
            
            // Öğrenciyi ekle
            System.out.println("Öğrenci ekleniyor...");
            studentManager.addStudent(newStudent);
            
            // Tüm öğrencileri listele
            System.out.println("\nTüm öğrenciler:");
            List<Student> students = studentManager.getAllStudents();
            for (Student student : students) {
                System.out.println(student);
            }
            
            // Öğrenciyi güncelle
            System.out.println("\nÖğrenci güncelleniyor...");
            newStudent = new Student(
                "TST001",
                "Test",
                "Öğrenci",
                21,
                90.0
            );
            studentManager.updateStudent(newStudent);
            
            // Güncellenmiş öğrenciyi göster
            System.out.println("\nGüncellenmiş öğrenci:");
            Student updatedStudent = studentManager.getStudent("TST001");
            System.out.println(updatedStudent);
            
            // Öğrenciyi sil
            System.out.println("\nÖğrenci siliniyor...");
            studentManager.deleteStudent("TST001");
            
            // Silindikten sonra tüm öğrencileri listele
            System.out.println("\nSilme işleminden sonra tüm öğrenciler:");
            students = studentManager.getAllStudents();
            for (Student student : students) {
                System.out.println(student);
            }
            
        } catch (DatabaseException e) {
            System.err.println("Hata: " + e.getMessage());
        }
    }
} 