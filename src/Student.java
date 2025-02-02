public class Student {
    private String studentID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int age;
    private double gpa;
    
    // Constructor
    public Student(String studentID, String firstName, String lastName, int age, double gpa) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gpa = gpa;
    }
    
    // Getter ve Setter metodları
    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { 
        if (studentID == null || !studentID.matches("^[0-9]{5,10}$")) {
            throw new IllegalArgumentException("Öğrenci ID'si 5-10 haneli bir sayı olmalıdır");
        }
        this.studentID = studentID; 
    }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { 
        if (firstName == null || !firstName.matches("^[a-zA-ZğüşıöçĞÜŞİÖÇ]{2,25}$")) {
            throw new IllegalArgumentException("İsim 2-25 karakter arasında olmalı ve sadece harflerden oluşmalıdır");
        }
        this.firstName = firstName; 
    }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { 
        if (lastName == null || !lastName.matches("^[a-zA-ZğüşıöçĞÜŞİÖÇ]{2,25}$")) {
            throw new IllegalArgumentException("Soyisim 2-25 karakter arasında olmalı ve sadece harflerden oluşmalıdır");
        }
        this.lastName = lastName; 
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public int getAge() { return age; }
    public void setAge(int age) { 
        if (age < 16 || age > 100) {
            throw new IllegalArgumentException("Yaş 16 ile 100 arasında olmalıdır");
        }
        this.age = age; 
    }
    
    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { 
        if (gpa < 0.0 || gpa > 100.0) {
            throw new IllegalArgumentException("Not ortalaması 0.0 ile 100.0 arasında olmalıdır");
        }
        this.gpa = gpa; 
    }
    
    @Override
    public String toString() {
        return String.format("ID: %s, Ad: %s, Soyad: %s, Email: %s, Telefon: %s, Yaş: %d, GPA: %.2f",
            studentID, firstName, lastName, email, phone, age, gpa);
    }
} 