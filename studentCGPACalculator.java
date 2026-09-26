import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class StudentCGPACalculatorGUI extends JFrame {

    private JTextField nameField;
    private JTextField studentIdField;
    private JTextField courseCodeField;
    private JTextField courseTitleField;
    private JTextField creditUnitField;

    private JComboBox<String> departmentBox;
    private JComboBox<String> gradeBox;
    private JComboBox<String> studentRecordBox;

    private JTextArea outputArea;

    private final ArrayList<Student> students;
    private Student currentStudent;

    public StudentCGPACalculatorGUI() {
        students = new ArrayList<>();

        setTitle("Student Course Registration and CGPA Calculator");
        setSize(950, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
        createSampleStudents();
    }

    private void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel headingLabel = new JLabel(
                "UNIVERSITY OF IBADAN - STUDENT CGPA CALCULATOR",
                SwingConstants.CENTER
        );

        headingLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(headingLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        formPanel.add(createStudentPanel());
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createCoursePanel());
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createButtonPanel());

        mainPanel.add(formPanel, BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(900, 300));

        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 8));
       