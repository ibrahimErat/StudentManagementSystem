public class Main {
    public static void main(String[] args) {
        // Swing uygulamasını Event Dispatch Thread üzerinde çalıştır
        javax.swing.SwingUtilities.invokeLater(() -> {
            new StudentManagementGUI();
        });
    }
} 