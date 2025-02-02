# Student Management System

A Java-based desktop application for managing student records with a modern graphical user interface.

## Features

- Add new students with details (ID, Full Name, Age, GPA)
- View all student records in a table format
- Edit existing student information
- Delete student records
- Search students by ID, name, or age
- Calculate class statistics (Average GPA, Average Age)
- View detailed student reports

## Requirements

- Java Development Kit (JDK) 8 or higher
- SQLite JDBC Driver (included in the project: sqlite-jdbc-3.42.0.0.jar)

## Project Structure

```
Project/
├── src/                    # Source code files
│   ├── Main.java          # Application entry point
│   ├── Student.java       # Student model class
│   ├── DatabaseConnection.java  # Database connection handler
│   └── StudentManagementGUI.java # Main GUI class
├── database/              # Database files
│   └── student_database.db # SQLite database file
├── lib/                   # External libraries
│   └── sqlite-jdbc-3.42.0.0.jar # SQLite JDBC driver
├── start.bat             # Batch file to compile and run (Windows)
└── README.md             # This file
```

## Installation & Setup

1. Clone or download the repository
2. Make sure you have JDK installed and JAVA_HOME environment variable is set
3. The SQLite database will be automatically created on first run

### Database Setup

The application will automatically create the necessary database and tables. However, if you need to create them manually, use the following SQL commands:

```sql
CREATE TABLE IF NOT EXISTS students (
    student_id TEXT PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    age INTEGER NOT NULL,
    gpa REAL NOT NULL,
    CONSTRAINT age_check CHECK (age >= 16 AND age <= 100),
    CONSTRAINT gpa_check CHECK (gpa >= 0 AND gpa <= 100)
);
```

## How to Run

### Using start.bat (Windows)

1. Double-click on `start.bat`
   - Or run from command prompt: `.\start.bat`

### Manual Compilation and Running

1. Open terminal/command prompt
2. Navigate to project directory
3. Compile the source files:
   ```bash
   cd src
   javac -cp "../sqlite-jdbc-3.42.0.0.jar" *.java
   ```
4. Run the application:
   ```bash
   java -cp ".;../sqlite-jdbc-3.42.0.0.jar" Main
   ```
   Note: On Unix-based systems, use : instead of ; in the classpath:
   ```bash
   java -cp ".:../sqlite-jdbc-3.42.0.0.jar" Main
   ```

## Usage Guide

1. **Adding a New Student**
   - Click on "New Student" tab
   - Fill in the required information:
     - Student ID (5 digits)
     - Full Name (letters only)
     - Age (16-100)
     - GPA (0-100)
   - Click "Add" button

2. **Viewing Students**
   - All students are displayed in the main table
   - Use "View All Students" button for detailed view
   - Click "Calculate GPA" for class statistics

3. **Searching Students**
   - Use the search box above the table
   - Search by ID, name, or age

4. **Editing/Deleting Students**
   - Use Edit/Delete buttons in the Actions column
   - Or select a student and use the top buttons

## Error Handling

- The application includes validation for all input fields
- Error messages are displayed in the message area
- Database errors are caught and displayed to the user

## Contributing

Feel free to fork the project and submit pull requests for any improvements.

## License

This project is open source and available under the MIT License. 