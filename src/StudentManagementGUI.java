/**
 * StudentManagementGUI - Main GUI class for the Student Management System
 * 
 * This class implements the graphical user interface for managing student records.
 * It provides functionality for:
 * - Adding new students
 * - Viewing student records
 * - Editing existing students
 * - Deleting students
 * - Searching students
 * - Calculating class statistics
 */
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class StudentManagementGUI extends JFrame {
    // GUI Components
    private JTextField idField, firstNameField, lastNameField, emailField, phoneField, gpaField;
    private JButton addButton, updateButton, deleteButton, searchButton;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextArea messageArea;
    private JTextField ageField;
    private JTextArea listTabMessageArea; // Message area for Student List tab
    private JTextArea newStudentTabMessageArea; // Message area for New Student tab
    
    // Color scheme constants
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    
    /**
     * Constructor - Initializes the main application window
     */
    public StudentManagementGUI() {
        setTitle("Student Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Initialize GUI components
        initComponents();
        
        // Set up event listeners
        setupEventListeners();
        
        // Create tabbed interface
        setupTabbedPane();
        
        // Load initial data
        refreshTable();
        
        setVisible(true);
    }
    
    private void initComponents() {
        // Form alanları
        idField = createStyledTextField(10);
        firstNameField = createStyledTextField(20);
        lastNameField = createStyledTextField(20);
        emailField = createStyledTextField(20);
        phoneField = createStyledTextField(15);
        gpaField = createStyledTextField(5);
        ageField = createStyledTextField(5);
        
        // Butonlar
        addButton = createStyledButton("Add", null);
        updateButton = createStyledButton("Update", null);
        deleteButton = createStyledButton("Delete", null);
        searchButton = createStyledButton("Search", null);
        
        // Tablo modeli
        String[] columnNames = {"ID", "Full Name", "Age", "GPA", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Sadece İşlemler sütunu düzenlenebilir
            }
        };
        studentTable = new JTable(tableModel);
        styleTable(studentTable);
        
        // Mesaj alanı
        messageArea = new JTextArea(3, 40);
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 12));
        messageArea.setBackground(new Color(245, 245, 245));
        messageArea.setForeground(TEXT_COLOR);
        messageArea.setOpaque(true);
    }
    
    /**
     * Creates a styled text field with consistent appearance
     * @param columns Number of columns in the text field
     * @return Styled JTextField instance
     */
    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));
        return field;
    }
    
    /**
     * Creates a styled button with hover effects
     * @param text Button text
     * @param icon Button icon (optional)
     * @return Styled JButton instance
     */
    private JButton createStyledButton(String text, Icon icon) {
        JButton button = new JButton(text);
        button.setIcon(icon);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorder(new RoundedBorder(20)); // Oval kenarlar için özel border
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false); // Arka planı şeffaf yap
        
        // Hover efekti
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
            
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(36, 113, 163));
            }
            
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }
    
    private void styleTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setIntercellSpacing(new Dimension(10, 10));
        table.setGridColor(new Color(230, 230, 230));
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Seçim rengini özelleştir
        table.setSelectionBackground(new Color(240, 247, 250));
        table.setSelectionForeground(new Color(44, 62, 80));
        
        // Başlık stili
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));
        
        // Alternatif satır renkleri ve hücre stili
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                    // Seçili satır için hafif bir border ekle
                    ((JLabel) c).setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(200, 220, 240)),
                        BorderFactory.createEmptyBorder(0, 5, 0, 5)
                    ));
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 249, 249));
                    c.setForeground(TEXT_COLOR);
                    // Normal satırlar için sadece padding ekle
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                }
                
                // Hücre içeriğini ortala
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                
                return c;
            }
        });
        
        // Fare üzerine gelince efekt
        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row > -1 && row != table.getSelectedRow()) {
                    table.clearSelection();
                    table.setRowSelectionInterval(row, row);
                }
            }
        });
        
        // Fare çıkınca seçimi kaldır
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                if (!table.isEditing()) {
                    table.clearSelection();
                }
            }
        });
    }
    
    private void setupTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(TEXT_COLOR);
        
        // Öğrenci Listesi Sekmesi
        JPanel listPanel = createStudentListPanel();
        tabbedPane.addTab("Student List", new ImageIcon("src/icons/list.png"), listPanel);
        
        // Yeni Öğrenci Sekmesi
        JPanel newStudentPanel = createNewStudentPanel();
        tabbedPane.addTab("New Student", new ImageIcon("src/icons/add.png"), newStudentPanel);
        
        // Ana panele ekle
        setContentPane(tabbedPane);
    }
    
    private JPanel createStudentListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Ana panel (tablo ve çıktı panelini içerecek)
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        
        // Üst panel (Arama ve Butonlar)
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(240, 240, 240), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Arama paneli
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        searchPanel.setBackground(Color.WHITE);
        
        // Özel arama alanı oluştur
        JTextField searchField = new JTextField(25) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
                
                g2.setColor(new Color(200, 200, 200, 120));
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
                
                super.paintComponent(g);
            }
        };
        
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setBackground(Color.WHITE);
        searchField.setOpaque(false);
        searchField.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        searchField.setPreferredSize(new Dimension(300, 35));
        
        // Arama alanı için DocumentListener ekle
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable(searchField.getText());
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable(searchField.getText());
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable(searchField.getText());
            }
        });
        
        searchPanel.add(new JLabel("Search Student: "));
        searchPanel.add(searchField);
        
        // Butonlar paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        // Calculate GPA button
        JButton calculateAverageButton = new JButton("Calculate GPA");
        calculateAverageButton.setFont(new Font("Arial", Font.BOLD, 13));
        calculateAverageButton.setBackground(new Color(52, 152, 219));
        calculateAverageButton.setForeground(Color.WHITE);
        calculateAverageButton.setBorder(new RoundedBorder(25));
        calculateAverageButton.setFocusPainted(false);
        calculateAverageButton.setContentAreaFilled(false);
        calculateAverageButton.setOpaque(true);
        calculateAverageButton.setPreferredSize(new Dimension(180, 35));
        
        // View All Students button
        JButton viewAllButton = new JButton("View All Students");
        viewAllButton.setFont(new Font("Arial", Font.BOLD, 13));
        viewAllButton.setBackground(PRIMARY_COLOR);
        viewAllButton.setForeground(Color.WHITE);
        viewAllButton.setBorder(new RoundedBorder(25));
        viewAllButton.setFocusPainted(false);
        viewAllButton.setContentAreaFilled(false);
        viewAllButton.setOpaque(true);
        viewAllButton.setPreferredSize(new Dimension(200, 35));
        
        // Not ortalaması hesaplama butonu işlevi
        calculateAverageButton.addActionListener(e -> {
            try {
                List<Student> students = DatabaseConnection.getAllStudents();
                if (students.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "No registered students found.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                double avgGpa = students.stream()
                    .mapToDouble(Student::getGpa)
                    .average()
                    .orElse(0.0);
                
                // En yüksek ve en düşük notları bul
                double maxGpa = students.stream()
                    .mapToDouble(Student::getGpa)
                    .max()
                    .orElse(0.0);
                
                double minGpa = students.stream()
                    .mapToDouble(Student::getGpa)
                    .min()
                    .orElse(0.0);
                
                // Sonuçları göster
                String message = String.format(
                    "Class Grade Statistics:\n\n" +
                    "Overall GPA: %.2f\n" +
                    "Highest Grade: %.2f\n" +
                    "Lowest Grade: %.2f\n" +
                    "Total Number of Students: %d",
                    avgGpa, maxGpa, minGpa, students.size()
                );
                
                JOptionPane.showMessageDialog(this,
                    message,
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                showMessage("Error: " + ex.getMessage());
            }
        });
        
        // Tüm öğrencileri görüntüleme butonu işlevi
        viewAllButton.addActionListener(e -> showAllStudentsDialog());
        
        buttonPanel.add(calculateAverageButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(viewAllButton);
        
        // Üst panele ekle
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Tablo ve Çıktı Paneli için Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.6); // Tablo %60, Çıktı %40 olarak değiştirildi
        splitPane.setDividerSize(8); // Ayırıcı çizgi kalınlığını artır
        splitPane.setBorder(null);
        
        // Tablo Paneli
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        
        // Tablo modelini güncelle
        String[] columnNames = {"ID", "Full Name", "Age", "GPA", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        
        studentTable = new JTable(tableModel);
        styleTable(studentTable);
        
        studentTable.getColumnModel().getColumn(4).setCellRenderer(new TableButtonRenderer());
        studentTable.getColumnModel().getColumn(4).setCellEditor(
            new TableButtonEditor(new JCheckBox(), this));
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Çıktı Paneli
        JPanel outputPanel = createOutputPanel();
        
        // Split pane'e ekle
        splitPane.setTopComponent(tablePanel);
        splitPane.setBottomComponent(outputPanel);
        
        // Ana panele ekle
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        panel.add(mainPanel);
        
        return panel;
    }
    
    private void showAllStudentsDialog() {
        JDialog dialog = new JDialog(this, "All Student Records", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        
        // Metin alanı
        JTextArea displayArea = new JTextArea();
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        displayArea.setEditable(false);
        displayArea.setMargin(new Insets(10, 10, 10, 10));
        displayArea.setBackground(Color.WHITE);
        
        // Kaydırma paneli
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        try {
            List<Student> students = DatabaseConnection.getAllStudents();
            
            // Başlık
            displayArea.append(String.format("%-10s %-30s %-10s %-15s\n",
                "ID", "Full Name", "Age", "GPA"));
            displayArea.append("-".repeat(70) + "\n");
            
            // Öğrenci bilgileri
            for (Student student : students) {
                String fullName = student.getFirstName() + " " + student.getLastName();
                displayArea.append(String.format("%-10s %-30s %-10d %-15.2f\n",
                    student.getStudentID(),
                    fullName,
                    student.getAge(),
                    student.getGpa()));
            }
            
            // İstatistikler
            displayArea.append("\nSTATISTICS\n");
            displayArea.append("-".repeat(70) + "\n");
            displayArea.append(String.format("Total Number of Students: %d\n", students.size()));
            
            // Yaş ortalaması
            double avgAge = students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
            displayArea.append(String.format("Average Age: %.1f\n", avgAge));
            
            // Not ortalaması
            double avgGpa = students.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);
            displayArea.append(String.format("Overall GPA: %.2f\n", avgGpa));
            
        } catch (Exception ex) {
            displayArea.setText("Error: " + ex.getMessage());
        }
        
        // Kapat butonu
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(PRIMARY_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorder(new RoundedBorder(20));
        closeButton.setFocusPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setOpaque(true);
        closeButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(closeButton);
        
        // Bileşenleri pencereye ekle
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private JPanel createNewStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form paneli
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Başlık ekle
        JLabel titleLabel = new JLabel("New Student Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form bileşenleri
        JLabel[] labels = {
            createStyledLabel("Student ID:"),
            createStyledLabel("Full Name:"),
            createStyledLabel("Age:"),
            createStyledLabel("GPA:")
        };
        
        JTextField[] fields = {
            idField, firstNameField, ageField, gpaField
        };
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1; // Başlıktan sonra başla
            gbc.weightx = 0.1;
            formPanel.add(labels[i], gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 0.9;
            formPanel.add(fields[i], gbc);
        }
        
        // Buton paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        addButton.setPreferredSize(new Dimension(200, 40));
        buttonPanel.add(addButton);
        
        // Form ve buton panelini birleştir
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Çıktı paneli
        JPanel outputPanel = createOutputPanel();
        
        // Ana panel oluştur ve bileşenleri ekle
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
        
        splitPane.setTopComponent(contentPanel);
        splitPane.setBottomComponent(outputPanel);
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(TEXT_COLOR);
        return label;
    }
    
    private void setupEventListeners() {
        // ID alanı için karakter kontrolü
        idField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String currentText = idField.getText();
                
                // Silme tuşlarına izin ver
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
                    return;
                }
                
                // Sadece rakam kontrolü
                if (!Character.isDigit(c)) {
                    e.consume();
                }
                
                // 5 rakamdan fazla girilmesini engelle
                if (currentText.length() >= 5) {
                    e.consume();
                }
            }
        });
        
        // Ad Soyad alanı için karakter kontrolü
        firstNameField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String currentText = firstNameField.getText();
                
                // Silme tuşlarına izin ver
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
                    return;
                }
                
                // 100 karakterden fazla girilmesini engelle
                if (currentText.length() >= 100) {
                    e.consume();
                    return;
                }
                
                // Sadece harf ve boşluk kontrolü
                if (!Character.isLetter(c) && !Character.isSpaceChar(c)) {
                    e.consume();
                }
                
                // Türkçe karakterlere izin ver
                String turkishChars = "ğüşıöçĞÜŞİÖÇ";
                if (turkishChars.indexOf(c) >= 0) {
                    return;
                }
            }
        });
        
        // Yaş alanı için karakter kontrolü
        ageField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String currentText = ageField.getText();
                
                // Silme tuşlarına izin ver
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
                    return;
                }
                
                // Sadece rakam kontrolü
                if (!Character.isDigit(c)) {
                    e.consume();
                    return;
                }
                
                // İlk rakam 0 olamaz
                if (currentText.isEmpty() && c == '0') {
                    e.consume();
                    return;
                }
                
                // 3 rakamdan fazla girilmesini engelle
                if (currentText.length() >= 3) {
                    e.consume();
                    return;
                }
                
                // 100'den büyük sayı girilmesini engelle
                String newText = currentText + c;
                try {
                    int age = Integer.parseInt(newText);
                    if (age > 100) {
                        e.consume();
                    }
                } catch (NumberFormatException ex) {
                    e.consume();
                }
            }
        });
        
        // Telefon numarası için karakter kontrolü
        phoneField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                try {
                    char c = evt.getKeyChar();
                    
                    // Silme tuşlarına her zaman izin ver
                    if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
                        return;
                    }
                    
                    String currentText = phoneField.getText();
                    
                    // Maksimum uzunluk kontrolü
                    if (currentText.length() >= 15) {
                        evt.consume();
                        return;
                    }
                    
                    // İzin verilen karakterler
                    boolean isValidChar = Character.isDigit(c) || 
                                        c == '+' || 
                                        c == '-' || 
                                        c == '(' || 
                                        c == ')' || 
                                        c == ' ';
                    
                    if (!isValidChar) {
                        evt.consume();
                    }
                } catch (Exception ex) {
                    evt.consume();
                }
            }
        });
        
        // Ekle butonu için event listener
        addButton.addActionListener(e -> {
            try {
                if (validateInputs()) {
                    Student student = createStudentFromFields();
                    DatabaseConnection.addStudent(student);
                    
                    // Detaylı başarı mesajı
                    StringBuilder message = new StringBuilder();
                    message.append("Yeni öğrenci başarıyla eklendi:\n");
                    message.append("ID: ").append(student.getStudentID()).append("\n");
                    message.append("Ad Soyad: ").append(student.getFirstName()).append(" ").append(student.getLastName()).append("\n");
                    message.append("Yaş: ").append(student.getAge()).append("\n");
                    message.append("Not Ortalaması: ").append(String.format("%.2f", student.getGpa()));
                    
                    showMessage(message.toString());
                    clearFields();
                    refreshTable();
                }
            } catch (DatabaseException ex) {
                showMessage("Database Error: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                showMessage("Please enter a valid GPA (numbers only).");
                gpaField.requestFocus();
            } catch (Exception ex) {
                showMessage("Unexpected error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        
        // Güncelle butonu için event listener
        updateButton.addActionListener(e -> {
            try {
                if (validateInputs()) {
                    Student oldStudent = DatabaseConnection.getStudent(idField.getText().trim());
                    Student newStudent = createStudentFromFields();
                    DatabaseConnection.updateStudent(newStudent);
                    
                    // Değişiklikleri göster
                    StringBuilder message = new StringBuilder();
                    message.append("Öğrenci başarıyla güncellendi:\n");
                    message.append("ID: ").append(newStudent.getStudentID()).append("\n");
                    
                    if (!oldStudent.getFirstName().equals(newStudent.getFirstName()) || 
                        !oldStudent.getLastName().equals(newStudent.getLastName())) {
                        message.append("Ad Soyad: ").append(oldStudent.getFirstName()).append(" ")
                               .append(oldStudent.getLastName()).append(" → ")
                               .append(newStudent.getFirstName()).append(" ")
                               .append(newStudent.getLastName()).append("\n");
                    }
                    
                    if (oldStudent.getAge() != newStudent.getAge()) {
                        message.append("Yaş: ").append(oldStudent.getAge())
                               .append(" → ").append(newStudent.getAge()).append("\n");
                    }
                    
                    if (oldStudent.getGpa() != newStudent.getGpa()) {
                        message.append("Not Ortalaması: ").append(String.format("%.2f", oldStudent.getGpa()))
                               .append(" → ").append(String.format("%.2f", newStudent.getGpa())).append("\n");
                    }
                    
                    showMessage(message.toString());
                    clearFields();
                    refreshTable();
                }
            } catch (DatabaseException ex) {
                showMessage("Database Error: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                showMessage("Please enter a valid GPA (numbers only).");
                gpaField.requestFocus();
            } catch (Exception ex) {
                showMessage("Unexpected error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        
        // Sil butonu için event listener
        deleteButton.addActionListener(e -> {
            try {
                String id = idField.getText().trim();
                if (id.isEmpty()) {
                    showMessage("Please enter the ID of the student to delete.");
                    idField.requestFocus();
                    return;
                }
                
                Student student = DatabaseConnection.getStudent(id);
                if (student == null) {
                    showMessage("Student with the specified ID not found: " + id);
                    return;
                }
                
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    String.format("Are you sure you want to delete this student?\n\n" +
                                "ID: %s\n" +
                                "Ad Soyad: %s %s\n" +
                                "Yaş: %d\n" +
                                "Not Ortalaması: %.2f",
                                student.getStudentID(),
                                student.getFirstName(),
                                student.getLastName(),
                                student.getAge(),
                                student.getGpa()),
                    "Student Deletion Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (choice == JOptionPane.YES_OPTION) {
                    DatabaseConnection.deleteStudent(id);
                    showMessage(String.format("Student deleted successfully:\n" +
                                           "ID: %s\n" +
                                           "Ad Soyad: %s %s",
                                           student.getStudentID(),
                                           student.getFirstName(),
                                           student.getLastName()));
                    clearFields();
                    refreshTable();
                }
            } catch (DatabaseException ex) {
                showMessage("Database Error: " + ex.getMessage());
            } catch (Exception ex) {
                showMessage("Unexpected error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        
        // Ara butonu için event listener
        searchButton.addActionListener(e -> {
            try {
                String id = idField.getText().trim();
                if (id.isEmpty()) {
                    showMessage("Please enter the ID of the student to search.");
                    idField.requestFocus();
                    return;
                }
                
                Student student = DatabaseConnection.getStudent(id);
                if (student == null) {
                    showMessage("Student with the specified ID not found: " + id);
                    return;
                }
                
                fillFieldsWithStudent(student);
                showMessage(String.format("Student found:\n" +
                                       "ID: %s\n" +
                                       "Ad Soyad: %s %s\n" +
                                       "Yaş: %d\n" +
                                       "Not Ortalaması: %.2f",
                                       student.getStudentID(),
                                       student.getFirstName(),
                                       student.getLastName(),
                                       student.getAge(),
                                       student.getGpa()));
                
            } catch (DatabaseException ex) {
                showMessage("Database Error: " + ex.getMessage());
                clearFields();
            } catch (Exception ex) {
                showMessage("Unexpected error: " + ex.getMessage());
                ex.printStackTrace();
                clearFields();
            }
        });
        
        // Form alanları için event listener'lar
        DocumentListener formListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkFormFields();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                checkFormFields();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                checkFormFields();
            }
        };
        
        idField.getDocument().addDocumentListener(formListener);
        firstNameField.getDocument().addDocumentListener(formListener);
        ageField.getDocument().addDocumentListener(formListener);
        gpaField.getDocument().addDocumentListener(formListener);

        // Not ortalaması için karakter kontrolü
        gpaField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String currentText = gpaField.getText();
                
                // Silme tuşlarına izin ver
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
                    return;
                }
                
                // Sadece rakam ve nokta kontrolü
                if (!Character.isDigit(c) && c != '.') {
                    e.consume();
                    return;
                }
                
                // Nokta kontrolü - sadece bir tane nokta olabilir
                if (c == '.' && currentText.contains(".")) {
                    e.consume();
                    return;
                }
                
                // Noktadan sonra en fazla 2 basamak
                if (currentText.contains(".")) {
                    int dotIndex = currentText.indexOf(".");
                    if (currentText.length() - dotIndex > 2) {
                        e.consume();
                        return;
                    }
                }
                
                // 3 basamaktan fazla tam sayı kısmı olamaz (100'den büyük olamaz)
                if (!currentText.contains(".")) {
                    if (currentText.length() >= 3) {
                        e.consume();
                        return;
                    }
                }
                
                // 100'den büyük sayı girilmesini engelle
                String newText = currentText + c;
                try {
                    if (!newText.endsWith(".")) {
                        double gpa = Double.parseDouble(newText);
                        if (gpa > 100) {
                            e.consume();
                        }
                    }
                } catch (NumberFormatException ex) {
                    e.consume();
                }
            }
        });
    }
    
    // Form alanlarının durumunu kontrol et
    private void checkFormFields() {
        boolean allFieldsFilled = !idField.getText().trim().isEmpty() &&
                                !firstNameField.getText().trim().isEmpty() &&
                                !ageField.getText().trim().isEmpty() &&
                                !gpaField.getText().trim().isEmpty();
        
        addButton.setEnabled(allFieldsFilled);
        updateButton.setEnabled(allFieldsFilled);
        deleteButton.setEnabled(!idField.getText().trim().isEmpty());
        searchButton.setEnabled(!idField.getText().trim().isEmpty());
    }
    
    /**
     * Creates a student object from the form fields
     * @return Student object with data from input fields
     */
    private Student createStudentFromFields() {
        String[] nameParts = firstNameField.getText().trim().split("\\s+", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        return new Student(
            idField.getText().trim(),
            firstName,
            lastName,
            Integer.parseInt(ageField.getText().trim()),
            Double.parseDouble(gpaField.getText().trim().isEmpty() ? "0.0" : gpaField.getText().trim())
        );
    }
    
    private void fillFieldsWithStudent(Student student) {
        idField.setText(student.getStudentID());
        firstNameField.setText(student.getFirstName() + " " + student.getLastName());
        ageField.setText(String.valueOf(student.getAge()));
        gpaField.setText(String.valueOf(student.getGpa()));
    }
    
    /**
     * Refreshes the student table with current database data
     */
    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<Student> students = DatabaseConnection.getAllStudents();
            for (Student student : students) {
                Object[] row = {
                    student.getStudentID(),
                    student.getFirstName() + " " + student.getLastName(),
                    student.getAge(),
                    student.getGpa(),
                    "İşlemler"
                };
                tableModel.addRow(row);
            }
            
            // Sütun genişliklerini ayarla
            studentTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
            studentTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Ad Soyad
            studentTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Yaş
            studentTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Not Ort.
            studentTable.getColumnModel().getColumn(4).setPreferredWidth(200); // İşlemler
            
            // İşlemler sütunu için özel renderer ve editor
            studentTable.getColumnModel().getColumn(4).setCellRenderer(new TableButtonRenderer());
            studentTable.getColumnModel().getColumn(4).setCellEditor(
                new TableButtonEditor(new JCheckBox(), this));
            
            // Tablo görünümünü güncelle
            studentTable.revalidate();
            studentTable.repaint();
            
        } catch (Exception ex) {
            showMessage("Error updating table: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Displays a message in the appropriate message area
     * @param message The message to display
     */
    private void showMessage(String message) {
        // Mesaj tipini belirle
        String icon;
        Color messageColor;

        if (message.toLowerCase().contains("error")) {
            icon = "❌";
            messageColor = new Color(231, 76, 60); // Red
        } else if (message.toLowerCase().contains("updated")) {
            icon = "🔄";
            messageColor = new Color(52, 152, 219); // Blue
        } else if (message.toLowerCase().contains("deleted")) {
            icon = "🗑️";
            messageColor = new Color(155, 89, 182); // Purple
        } else if (message.toLowerCase().contains("added")) {
            icon = "✅";
            messageColor = new Color(46, 204, 113); // Green
        } else {
            icon = "ℹ️";
            messageColor = new Color(52, 73, 94); // Dark gray
        }

        // Mesajı formatla
        String formattedMessage = String.format("[%s] %s %s", getCurrentTime(), icon, message);
        
        // Aktif sekmeye göre mesajı göster
        JTabbedPane tabbedPane = (JTabbedPane) getContentPane();
        int selectedIndex = tabbedPane.getSelectedIndex();
        
        if (selectedIndex == 0) { // Öğrenci Listesi sekmesi
            updateMessageArea(listTabMessageArea, formattedMessage, messageColor);
        } else if (selectedIndex == 1) { // Yeni Öğrenci sekmesi
            updateMessageArea(newStudentTabMessageArea, formattedMessage, messageColor);
        }
    }

    private void updateMessageArea(JTextArea area, String formattedMessage, Color messageColor) {
        if (area != null) {
            area.setForeground(messageColor);
            area.append(formattedMessage + "\n");
            area.setCaretPosition(area.getDocument().getLength());
        }
    }
    
    /**
     * Validates all input fields before database operations
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateInputs() {
        try {
            // ID validation
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                showMessage("Student ID is required.");
                idField.requestFocus();
                return false;
            }
            if (!id.matches("^[0-9]+$")) {
                showMessage("ID must contain only numbers.");
                idField.requestFocus();
                return false;
            }
            if (id.length() < 5) {
                showMessage("ID is too short. You entered " + id.length() + " digits, but 5 digits are required.");
                idField.requestFocus();
                return false;
            }
            if (id.length() > 5) {
                showMessage("ID is too long. You entered " + id.length() + " digits, but 5 digits are required.");
                idField.requestFocus();
                return false;
            }
            
            // Full Name validation
            String fullName = firstNameField.getText().trim();
            if (fullName.isEmpty()) {
                showMessage("Student name is required.");
                firstNameField.requestFocus();
                return false;
            }
            if (fullName.length() > 100) {
                showMessage("Name cannot exceed 100 characters. You entered " + fullName.length() + " characters.");
                firstNameField.requestFocus();
                return false;
            }
            if (!fullName.matches("^[a-zA-ZğüşıöçĞÜŞİÖÇ\\s]+$")) {
                showMessage("Name can only contain letters. Numbers and special characters are not allowed.");
                firstNameField.requestFocus();
                return false;
            }
            if (fullName.length() < 5) {
                showMessage("Name is too short. Please enter at least 5 characters.");
                firstNameField.requestFocus();
                return false;
            }
            
            // Age validation
            String ageStr = ageField.getText().trim();
            if (ageStr.isEmpty()) {
                showMessage("Age is required.");
                ageField.requestFocus();
                return false;
            }
            if (!ageStr.matches("^[0-9]+$")) {
                showMessage("Age must contain only numbers.");
                ageField.requestFocus();
                return false;
            }
            if (ageStr.startsWith("0")) {
                showMessage("Age cannot start with 0.");
                ageField.requestFocus();
                return false;
            }
            try {
                int age = Integer.parseInt(ageStr);
                if (age <= 0) {
                    showMessage("Age must be greater than 0.");
                    ageField.requestFocus();
                    return false;
                }
                if (age > 100) {
                    showMessage("Age cannot be greater than 100. You entered " + age + ".");
                    ageField.requestFocus();
                    return false;
                }
                if (age < 16) {
                    showMessage("Age must be at least 16. You entered " + age + ".");
                    ageField.requestFocus();
                    return false;
                }
            } catch (NumberFormatException ex) {
                showMessage("Please enter a valid age (numbers only).");
                ageField.requestFocus();
                return false;
            }
            
            // GPA validation
            String gpaStr = gpaField.getText().trim();
            if (gpaStr.isEmpty()) {
                showMessage("GPA is required.");
                gpaField.requestFocus();
                return false;
            }
            if (!gpaStr.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
                showMessage("Invalid GPA format. Example: 85.5");
                gpaField.requestFocus();
                return false;
            }
            try {
                double gpa = Double.parseDouble(gpaStr);
                if (gpa < 0) {
                    showMessage("GPA cannot be less than 0.");
                    gpaField.requestFocus();
                    return false;
                }
                if (gpa > 100) {
                    showMessage("GPA cannot be greater than 100. You entered " + gpa + ".");
                    gpaField.requestFocus();
                    return false;
                }
                if (gpa == 0) {
                    int choice = JOptionPane.showConfirmDialog(
                        this,
                        "GPA is entered as 0. Is this correct?",
                        "GPA Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    if (choice != JOptionPane.YES_OPTION) {
                        gpaField.requestFocus();
                        return false;
                    }
                }
            } catch (NumberFormatException ex) {
                showMessage("Please enter a valid GPA (e.g., 85.5)");
                gpaField.requestFocus();
                return false;
            }
            
            return true;
            
        } catch (Exception ex) {
            showMessage("Error during data validation: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    private void clearFields() {
        SwingUtilities.invokeLater(() -> {
            idField.setText("");
            firstNameField.setText("");
            ageField.setText("");
            gpaField.setText("");
            
            if (studentTable != null) {
                studentTable.clearSelection();
            }
            
            this.requestFocus();
            showMessage("Form cleared.");
        });
    }
    
    // TableButtonRenderer sınıfını güncelle
    private class TableButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton editButton;
        private JButton deleteButton;
        
        public TableButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
            setOpaque(false);
            
            editButton = new JButton("Edit");
            deleteButton = new JButton("Delete");
            
            styleButton(editButton, new Color(46, 204, 113));
            styleButton(deleteButton, new Color(231, 76, 60));
            
            add(editButton);
            add(deleteButton);
        }
        
        private void styleButton(JButton button, Color color) {
            button.setPreferredSize(new Dimension(90, 30));
            button.setBackground(color);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(new RoundedBorder(30)); // Oval kenarlar için daha büyük radius
            button.setContentAreaFilled(false);
            button.setOpaque(true);
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Hover efekti
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(color.brighter());
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(color);
                }
                
                @Override
                public void mousePressed(MouseEvent e) {
                    button.setBackground(color.darker());
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    button.setBackground(color);
                }
            });
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    // TableButtonEditor sınıfını güncelle
    private class TableButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton editButton;
        private JButton deleteButton;
        private String studentId;
        private JTable table;
        
        public TableButtonEditor(JCheckBox checkBox, JFrame parent) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            panel.setOpaque(false);
            
            editButton = new JButton("Edit");
            deleteButton = new JButton("Delete");
            
            styleButton(editButton, new Color(46, 204, 113));
            styleButton(deleteButton, new Color(231, 76, 60));
            
            editButton.addActionListener(e -> editStudent());
            deleteButton.addActionListener(e -> deleteStudent());
            
            panel.add(editButton);
            panel.add(deleteButton);
        }
        
        private void styleButton(JButton button, Color color) {
            button.setPreferredSize(new Dimension(90, 30));
            button.setBackground(color);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(new RoundedBorder(30));
            button.setContentAreaFilled(false);
            button.setOpaque(true);
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Hover efekti
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(color.brighter());
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(color);
                }
                
                @Override
                public void mousePressed(MouseEvent e) {
                    button.setBackground(color.darker());
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    button.setBackground(color);
                }
            });
        }
        
        private void editStudent() {
            try {
                Student student = DatabaseConnection.getStudent(studentId);
                if (student != null) {
                    showEditDialog(student);
                }
                fireEditingStopped();
            } catch (DatabaseException ex) {
                showMessage("Error: " + ex.getMessage());
            }
        }
        
        private void showEditDialog(Student student) {
            // Önceki değerleri kaydet
            String oldFirstName = student.getFirstName();
            String oldLastName = student.getLastName();
            int oldAge = student.getAge();
            double oldGpa = student.getGpa();

            // Ana pencereyi oluştur
            JDialog dialog = new JDialog(StudentManagementGUI.this, "Edit Student", true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(400, 350);
            dialog.setLocationRelativeTo(StudentManagementGUI.this);
            
            // Form paneli
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            formPanel.setBackground(Color.WHITE);
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            // Form alanları
            JTextField editIdField = createStyledTextField(15);
            JTextField editNameField = createStyledTextField(20);
            JTextField editAgeField = createStyledTextField(5);
            JTextField editGpaField = createStyledTextField(5);
            
            // Mevcut değerleri doldur
            editIdField.setText(student.getStudentID());
            editNameField.setText(student.getFirstName() + " " + student.getLastName());
            editAgeField.setText(String.valueOf(student.getAge()));
            editGpaField.setText(String.valueOf(student.getGpa()));
            
            // ID alanını devre dışı bırak
            editIdField.setEditable(false);
            editIdField.setBackground(new Color(245, 245, 245));
            
            // Etiketler
            String[] labels = {"ID:", "Full Name:", "Age:", "GPA:"};
            JTextField[] fields = {editIdField, editNameField, editAgeField, editGpaField};
            
            // Form elemanlarını ekle
            for (int i = 0; i < labels.length; i++) {
                JLabel label = new JLabel(labels[i]);
                label.setFont(new Font("Arial", Font.BOLD, 14));
                label.setForeground(TEXT_COLOR);
                
                gbc.gridx = 0;
                gbc.gridy = i;
                gbc.weightx = 0.2;
                formPanel.add(label, gbc);
                
                gbc.gridx = 1;
                gbc.weightx = 0.8;
                formPanel.add(fields[i], gbc);
            }
            
            // Buton paneli
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
            
            // Butonlar
            JButton saveButton = new JButton("Update");
            JButton cancelButton = new JButton("Cancel");
            
            // Buton stilleri
            saveButton.setPreferredSize(new Dimension(120, 40));
            saveButton.setBackground(new Color(46, 204, 113));
            saveButton.setForeground(Color.WHITE);
            saveButton.setFocusPainted(false);
            saveButton.setBorder(new RoundedBorder(30));
            saveButton.setContentAreaFilled(false);
            saveButton.setOpaque(true);
            saveButton.setFont(new Font("Arial", Font.BOLD, 14));
            
            cancelButton.setPreferredSize(new Dimension(120, 40));
            cancelButton.setBackground(new Color(231, 76, 60));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setFocusPainted(false);
            cancelButton.setBorder(new RoundedBorder(30));
            cancelButton.setContentAreaFilled(false);
            cancelButton.setOpaque(true);
            cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
            
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            
            // Kaydet butonu işlevi
            saveButton.addActionListener(e -> {
                try {
                    // Ad Soyad kontrolü
                    String fullName = editNameField.getText().trim();
                    if (fullName.isEmpty()) {
                        showMessage("Ad Soyad alanı boş bırakılamaz.");
                        editNameField.requestFocus();
                        return;
                    }
                    if (!fullName.matches("^[a-zA-ZğüşıöçĞÜŞİÖÇ\\s]{2,50}$")) {
                        showMessage("Ad Soyad sadece harflerden oluşmalı ve 2-50 karakter arasında olmalıdır.");
                        editNameField.requestFocus();
                        return;
                    }
                    
                    // Yaş kontrolü
                    String ageStr = editAgeField.getText().trim();
                    if (ageStr.isEmpty()) {
                        showMessage("Yaş alanı boş bırakılamaz.");
                        editAgeField.requestFocus();
                        return;
                    }
                    int age;
                    try {
                        age = Integer.parseInt(ageStr);
                        if (age < 16 || age > 100) {
                            showMessage("Yaş 16 ile 100 arasında olmalıdır.");
                            editAgeField.requestFocus();
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        showMessage("Geçerli bir yaş giriniz (sadece sayı).");
                        editAgeField.requestFocus();
                        return;
                    }
                    
                    // Not ortalaması kontrolü
                    String gpaStr = editGpaField.getText().trim();
                    if (gpaStr.isEmpty()) {
                        showMessage("Not ortalaması alanı boş bırakılamaz.");
                        editGpaField.requestFocus();
                        return;
                    }
                    double gpa;
                    try {
                        gpa = Double.parseDouble(gpaStr);
                        if (gpa < 0) {
                            showMessage("Not ortalaması 0'dan küçük olamaz.");
                            editGpaField.requestFocus();
                            return;
                        }
                        if (gpa > 100) {
                            showMessage("Not ortalaması 100'den büyük olamaz. Şu anda " + gpa + " girdiniz.");
                            editGpaField.requestFocus();
                            return;
                        }
                        if (gpa == 0) {
                            int choice = JOptionPane.showConfirmDialog(
                                SwingUtilities.getWindowAncestor(panel),
                                "Not ortalaması 0 olarak girildi. Bu değer doğru mu?",
                                "GPA Confirmation",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE
                            );
                            if (choice != JOptionPane.YES_OPTION) {
                                editGpaField.requestFocus();
                                return;
                            }
                        }
                    } catch (NumberFormatException ex) {
                        showMessage("Please enter a valid GPA (e.g., 85.5)");
                        editGpaField.requestFocus();
                        return;
                    }
                    
                    // Ad ve Soyadı ayır
                    String[] nameParts = fullName.split("\\s+", 2);
                    String firstName = nameParts[0];
                    String lastName = nameParts.length > 1 ? nameParts[1] : "";
                    
                    // Öğrenci nesnesini güncelle
                    Student updatedStudent = new Student(
                        editIdField.getText(),
                        firstName,
                        lastName,
                        age,
                        gpa
                    );
                    
                    // Değişiklikleri kontrol et ve mesaj oluştur
                    StringBuilder changes = new StringBuilder();
                    changes.append("Student updated (ID: ").append(updatedStudent.getStudentID()).append(")\n");
                    
                    if (!oldFirstName.equals(firstName) || !oldLastName.equals(lastName)) {
                        changes.append("Name: ").append(oldFirstName).append(" ").append(oldLastName)
                              .append(" → ").append(firstName).append(" ").append(lastName).append("\n");
                    }
                    if (oldAge != age) {
                        changes.append("Age: ").append(oldAge).append(" → ").append(age).append("\n");
                    }
                    if (oldGpa != gpa) {
                        changes.append("GPA: ").append(String.format("%.2f", oldGpa))
                              .append(" → ").append(String.format("%.2f", gpa)).append("\n");
                    }

                    // Veritabanını güncelle
                    DatabaseConnection.updateStudent(updatedStudent);
                    showMessage(changes.toString());
                    refreshTable();
                    dialog.dispose();
                    
                } catch (Exception ex) {
                    showMessage("Error: " + ex.getMessage());
                }
            });
            
            // İptal butonu işlevi
            cancelButton.addActionListener(e -> dialog.dispose());
            
            // Pencerenin içeriğini ekle
            dialog.add(formPanel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            // Pencereyi göster
            dialog.setVisible(true);
        }
        
        private void deleteStudent() {
            try {
                String studentId = table.getValueAt(table.getSelectedRow(), 0).toString();
                Student student = DatabaseConnection.getStudent(studentId);
                
                if (student != null) {
                    int choice = JOptionPane.showConfirmDialog(
                        StudentManagementGUI.this,
                        String.format("Are you sure you want to delete this student?\n\n" +
                                    "ID: %s\n" +
                                    "Ad Soyad: %s %s\n" +
                                    "Yaş: %d\n" +
                                    "Not Ortalaması: %.2f",
                                    student.getStudentID(),
                                    student.getFirstName(),
                                    student.getLastName(),
                                    student.getAge(),
                                    student.getGpa()),
                        "Student Deletion Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    
                    if (choice == JOptionPane.YES_OPTION) {
                        DatabaseConnection.deleteStudent(studentId);
                        showMessage(String.format("Student deleted successfully:\n" +
                                               "ID: %s\n" +
                                               "Ad Soyad: %s %s",
                                               student.getStudentID(),
                                               student.getFirstName(),
                                               student.getLastName()));
                        refreshTable();
                    }
                }
                fireEditingStopped();
            } catch (Exception ex) {
                showMessage("Error: " + ex.getMessage());
            }
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.table = table;
            this.studentId = table.getValueAt(row, 0).toString();
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
    
    // Oval kenarlı butonlar için özel border sınıfı
    private static class RoundedBorder extends AbstractBorder {
        private int radius;
        
        RoundedBorder(int radius) {
            this.radius = radius;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (c instanceof JButton) {
                JButton button = (JButton) c;
                
                // Arka plan için daha yumuşak bir oval
                g2.setColor(button.getBackground());
                g2.fillRoundRect(x, y, width-1, height-1, height, height); // Tam oval için height değerini kullan
                
                // Buton metnini çiz
                FontMetrics fm = g2.getFontMetrics();
                Rectangle textRect = new Rectangle(x, y, width - 1, height - 1);
                String buttonText = button.getText();
                g2.setColor(button.getForeground());
                int textX = textRect.x + (textRect.width - fm.stringWidth(buttonText)) / 2;
                int textY = textRect.y + ((textRect.height - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(buttonText, textX, textY);
            }
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            int value = Math.min(radius, c.getHeight()/2);
            return new Insets(value, value+5, value, value+5);
        }
        
        @Override
        public boolean isBorderOpaque() {
            return true;
        }
    }
    
    // Tablo filtreleme metodu
    private void filterTable(String searchText) {
        try {
            if (searchText.trim().isEmpty()) {
                refreshTable();
                return;
            }
            
            tableModel.setRowCount(0);
            List<Student> allStudents = DatabaseConnection.getAllStudents();
            String searchLower = searchText.toLowerCase().trim();
            
            for (Student student : allStudents) {
                String fullName = (student.getFirstName() + " " + student.getLastName()).toLowerCase();
                if (student.getStudentID().toLowerCase().contains(searchLower) ||
                    fullName.contains(searchLower) ||
                    String.valueOf(student.getAge()).contains(searchLower)) {
                    
                    Object[] row = {
                        student.getStudentID(),
                        student.getFirstName() + " " + student.getLastName(),
                        student.getAge(),
                        student.getGpa(),
                        "İşlemler"
                    };
                    tableModel.addRow(row);
                }
            }
            
            if (tableModel.getRowCount() == 0) {
                showMessage("Search result not found: " + searchText);
            }
            
        } catch (Exception ex) {
            showMessage("Error during search: " + ex.getMessage());
        }
    }
    
    private JPanel createOutputPanel() {
        JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
        outputPanel.setBackground(Color.WHITE);
        outputPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Başlık Paneli - Yüksekliği azalt
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0)); // Üst boşluğu azalt
        
        JLabel titleLabel = new JLabel("System Messages");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 13)); // Font boyutunu küçült
        titleLabel.setForeground(TEXT_COLOR);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0)); // Butonlar arası boşluğu azalt
        buttonPanel.setBackground(Color.WHITE);
        
        // Butonları küçült
        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.BOLD, 11)); // Font boyutunu küçült
        clearButton.setBackground(new Color(189, 195, 199));
        clearButton.setForeground(Color.WHITE);
        clearButton.setBorder(new RoundedBorder(12)); // Border radius'u küçült
        clearButton.setFocusPainted(false);
        clearButton.setContentAreaFilled(false);
        clearButton.setOpaque(true);
        clearButton.setPreferredSize(new Dimension(70, 25)); // Boyutu küçült
        
        JButton copyButton = new JButton("Copy");
        copyButton.setFont(new Font("Arial", Font.BOLD, 11)); // Font boyutunu küçült
        copyButton.setBackground(new Color(52, 152, 219));
        copyButton.setForeground(Color.WHITE);
        copyButton.setBorder(new RoundedBorder(12)); // Border radius'u küçült
        copyButton.setFocusPainted(false);
        copyButton.setContentAreaFilled(false);
        copyButton.setOpaque(true);
        copyButton.setPreferredSize(new Dimension(70, 25)); // Boyutu küçült
        
        buttonPanel.add(copyButton);
        buttonPanel.add(Box.createHorizontalStrut(3)); // Butonlar arası boşluğu azalt
        buttonPanel.add(clearButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Mesaj Alanı - Daha geniş alan
        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setEditable(false);
        area.setMargin(new Insets(12, 12, 12, 12)); // Kenar boşluklarını artır
        area.setBackground(new Color(250, 250, 250));
        area.setForeground(TEXT_COLOR);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        
        // Kaydırma Paneli
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Buton İşlevleri
        clearButton.addActionListener(e -> {
            area.setText("");
            if (area == listTabMessageArea) {
                newStudentTabMessageArea.setText("");
            } else {
                listTabMessageArea.setText("");
            }
        });
        
        copyButton.addActionListener(e -> {
            area.selectAll();
            area.copy();
            area.select(area.getCaretPosition(), area.getCaretPosition());
            showMessage("Messages copied to clipboard.");
        });
        
        outputPanel.add(headerPanel, BorderLayout.NORTH);
        outputPanel.add(scrollPane, BorderLayout.CENTER);
        
        if (listTabMessageArea == null) {
            listTabMessageArea = area;
        } else {
            newStudentTabMessageArea = area;
        }
        
        return outputPanel;
    }
    
    private String getCurrentTime() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
} 