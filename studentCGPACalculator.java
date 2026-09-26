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
        panel.setBorder(BorderFactory.createTitledBorder(
                "Student Information"
        ));

        studentRecordBox = new JComboBox<>();
        studentRecordBox.addItem("New Student");

        nameField = new JTextField();
        studentIdField = new JTextField();

        String[] departments = {
                "Computer Science",
                "Mathematics",
                "Physics"
        };

        departmentBox = new JComboBox<>(departments);

        panel.add(new JLabel("Select Student:"));
        panel.add(studentRecordBox);

        panel.add(new JLabel("Student Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Student ID:"));
        panel.add(studentIdField);

        panel.add(new JLabel("Department:"));
        panel.add(departmentBox);

        studentRecordBox.addActionListener(e -> loadSelectedStudent());

        return panel;
    }

    private JPanel createCoursePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 8));
        panel.setBorder(BorderFactory.createTitledBorder(
                "Course Information"
        ));

        courseCodeField = new JTextField();
        courseTitleField = new JTextField();
        creditUnitField = new JTextField();

        String[] grades = {"A", "B", "C", "D", "E", "F"};
        gradeBox = new JComboBox<>(grades);

        panel.add(new JLabel("Course Code:"));
        panel.add(courseCodeField);

        panel.add(new JLabel("Course Title:"));
        panel.add(courseTitleField);

        panel.add(new JLabel("Credit Unit:"));
        panel.add(creditUnitField);

        panel.add(new JLabel("Grade:"));
        panel.add(gradeBox);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));

        JButton saveStudentButton = new JButton("Save Student");
        JButton registerCourseButton = new JButton("Register Course");
        JButton calculateButton = new JButton("Calculate CGPA");
        JButton profileButton = new JButton("Display Profile");
        JButton allStudentsButton = new JButton("Display All Students");
        JButton clearButton = new JButton("Clear Form");
        JButton exitButton = new JButton("Exit");

        panel.add(saveStudentButton);
        panel.add(registerCourseButton);
        panel.add(calculateButton);
        panel.add(profileButton);
        panel.add(allStudentsButton);
        panel.add(clearButton);
        panel.add(exitButton);

        saveStudentButton.addActionListener(e -> saveStudent());
        registerCourseButton.addActionListener(e -> registerCourse());
        calculateButton.addActionListener(e -> calculateCGPA());
        profileButton.addActionListener(e -> displayProfile());
        allStudentsButton.addActionListener(e -> displayAllStudents());
        clearButton.addActionListener(e -> clearForm());

        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to exit the application?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        return panel;
    }

    private void saveStudent() {
        String name = nameField.getText().trim();
        String studentId = studentIdField.getText().trim();
        String department =
                departmentBox.getSelectedItem().toString();

        if (name.isEmpty() || studentId.isEmpty()) {
            showError("Please enter the student's name and ID.");
            return;
        }

        if (!studentId.startsWith("UI/")) {
            showError(
                    "Student ID must use the UI matriculation format.\n"
                    + "Example: UI/CSC/2026/001"
            );
            return;
        }

        Student existingStudent = findStudent(studentId);

        if (existingStudent != null && existingStudent != currentStudent) {
            showError("A student with this ID already exists.");
            return;
        }

        if (currentStudent == null) {
            currentStudent =
                    new Student(name, studentId, department);

            students.add(currentStudent);
            studentRecordBox.addItem(studentId);

            studentRecordBox.setSelectedItem(studentId);

            JOptionPane.showMessageDialog(
                    this,
                    "Student created successfully."
            );
        } else {
            currentStudent.setName(name);
            currentStudent.setStudentId(studentId);
            currentStudent.setDepartment(department);

            refreshStudentRecords();

            studentRecordBox.setSelectedItem(studentId);

            JOptionPane.showMessageDialog(
                    this,
                    "Student information updated successfully."
            );
        }
    }

    private void registerCourse() {
        if (currentStudent == null) {
            showError("Please save or select a student first.");
            return;
        }

        String courseCode =
                courseCodeField.getText().trim().toUpperCase();

        String courseTitle =
                courseTitleField.getText().trim();

        String creditText =
                creditUnitField.getText().trim();

        String grade =
                gradeBox.getSelectedItem().toString();

        if (courseCode.isEmpty()
                || courseTitle.isEmpty()
                || creditText.isEmpty()) {

            showError("Please complete all course fields.");
            return;
        }

        if (courseAlreadyRegistered(courseCode)) {
            showError("This course has already been registered.");
            return;
        }

        try {
            int creditUnit = Integer.parseInt(creditText);

            if (creditUnit <= 0) {
                showError("Credit unit must be a positive integer.");
                return;
            }

            Course course = new Course(
                    courseCode,
                    courseTitle,
                    creditUnit,
                    grade
            );

            currentStudent.registerCourse(course);

            JOptionPane.showMessageDialog(
                    this,
                    "Course registered successfully.\n"
                    + "Total registered courses: "
                    + currentStudent.getCourses().size()
            );

            clearCourseFields();
            displayProfile();

        } catch (NumberFormatException exception) {
            showError("Credit unit must be a valid integer.");
        }
    }

    private boolean courseAlreadyRegistered(String courseCode) {
        for (Course course : currentStudent.getCourses()) {
            if (course.getCourseCode().equalsIgnoreCase(courseCode)) {
                return true;
            }
        }

        return false;
    }

    private void calculateCGPA() {
        if (currentStudent == null) {
            showError("Please save or select a student first.");
            return;
        }

        if (currentStudent.getCourses().size() < 5) {
            showError(
                    "A student must register at least 5 courses "
                    + "before CGPA can be calculated."
            );
            return;
        }

        double cgpa = currentStudent.calculateCGPA();

        JOptionPane.showMessageDialog(
                this,
                String.format(
                        "%s's CGPA is %.2f",
                        currentStudent.getName(),
                        cgpa
                ),
                "CGPA Result",
                JOptionPane.INFORMATION_MESSAGE
        );

        displayProfile();
    }

    private void displayProfile() {
        if (currentStudent == null) {
            showError("Please save or select a student first.");
            return;
        }

        outputArea.setText(buildProfile(currentStudent));
        outputArea.setCaretPosition(0);
    }

    private String buildProfile(Student student) {
        StringBuilder profile = new StringBuilder();

        profile.append(
                "==============================================================\n"
        );
        profile.append(
                "       UNIVERSITY OF IBADAN - FACULTY OF COMPUTING\n"
        );
        profile.append(
                "                     STUDENT PROFILE\n"
        );
        profile.append(
                "==============================================================\n"
        );

        profile.append(String.format(
                "Student ID          : %s%n",
                student.getStudentId()
        ));

        profile.append(String.format(
                "Name                : %s%n",
                student.getName()
        ));

        profile.append(String.format(
                "Department          : %s%n",
                student.getDepartment()
        ));

        profile.append(String.format(
                "Courses Registered  : %d%n",
                student.getCourses().size()
        ));

        profile.append(
                "--------------------------------------------------------------\n"
        );

        profile.append(String.format(
                "%-12s %-28s %-5s %-6s%n",
                "Code",
                "Course Title",
                "CU",
                "Grade"
        ));

        profile.append(
                "--------------------------------------------------------------\n"
        );

        int totalCreditUnits = 0;

        for (Course course : student.getCourses()) {
            profile.append(String.format(
                    "%-12s %-28s %-5d %-6s%n",
                    course.getCourseCode(),
                    shortenTitle(course.getCourseTitle()),
                    course.getCreditUnit(),
                    course.getGrade()
            ));

            totalCreditUnits += course.getCreditUnit();
        }

        profile.append(
                "--------------------------------------------------------------\n"
        );

        profile.append(String.format(
                "Total Credit Units  : %d%n",
                totalCreditUnits
        ));

        if (student.getCourses().size() >= 5) {
            profile.append(String.format(
                    "CGPA                : %.2f%n",
                    student.calculateCGPA()
            ));
        } else {
            profile.append(
                    "CGPA                : Not available, fewer than 5 courses\n"
            );
        }

        profile.append(
                "==============================================================\n"
        );

        return profile.toString();
    }

    private String shortenTitle(String title) {
        if (title.length() > 27) {
            return title.substring(0, 24) + "...";
        }

        return title;
    }

    private void displayAllStudents() {
        if (students.isEmpty()) {
            showError("No student records are available.");
            return;
        }

        StringBuilder allProfiles = new StringBuilder();

        for (Student student : students) {
            allProfiles.append(buildProfile(student));
            allProfiles.append("\n\n");
        }

        outputArea.setText(allProfiles.toString());
        outputArea.setCaretPosition(0);
    }

    private Student findStudent(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equalsIgnoreCase(studentId)) {
                return student;
            }
        }

        return null;
    }

    private void loadSelectedStudent() {
        if (studentRecordBox.getSelectedIndex() <= 0) {
            currentStudent = null;
            return;
        }

        String selectedId =
                studentRecordBox.getSelectedItem().toString();

        Student selectedStudent = findStudent(selectedId);

        if (selectedStudent != null) {
            currentStudent = selectedStudent;

            nameField.setText(selectedStudent.getName());
            studentIdField.setText(selectedStudent.getStudentId());

            departmentBox.setSelectedItem(
                    selectedStudent.getDepartment()
            );

            outputArea.setText(buildProfile(selectedStudent));
        }
    }

    private void refreshStudentRecords() {
        String selectedId = currentStudent == null
                ? null
                : currentStudent.getStudentId();

        studentRecordBox.removeAllItems();
        studentRecordBox.addItem("New Student");

        for (Student student : students) {
            studentRecordBox.addItem(student.getStudentId());
        }

        if (selectedId != null) {
            studentRecordBox.setSelectedItem(selectedId);
        }
    }

    private void clearForm() {
        currentStudent = null;

        studentRecordBox.setSelectedIndex(0);
        nameField.setText("");
        studentIdField.setText("");
        departmentBox.setSelectedIndex(0);

        clearCourseFields();
        outputArea.setText("");

        nameField.requestFocus();
    }

    private void clearCourseFields() {
        courseCodeField.setText("");
        courseTitleField.setText("");
        creditUnitField.setText("");
        gradeBox.setSelectedIndex(0);

        courseCodeField.requestFocus();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Input Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void createSampleStudents() {
        Student computerScienceStudent = new Student(
                "John Ade",
                "UI/CSC/2026/001",
                "Computer Science"
        );

        computerScienceStudent.registerCourse(
                new Course("CSC201", "Data Structures", 3, "A")
        );
        computerScienceStudent.registerCourse(
                new Course("CSC203", "Computer Architecture", 3, "B")
        );
        computerScienceStudent.registerCourse(
                new Course("MTH201", "Mathematics II", 3, "A")
        );
        computerScienceStudent.registerCourse(
                new Course("CSC205", "Programming II", 3, "B")
        );
        computerScienceStudent.registerCourse(
                new Course("STA201", "Statistics", 2, "C")
        );

        Student mathematicsStudent = new Student(
                "Grace Bello",
                "UI/MTH/2026/002",
                "Mathematics"
        );

        mathematicsStudent.registerCourse(
                new Course("MTH201", "Mathematical Methods", 3, "A")
        );
        mathematicsStudent.registerCourse(
                new Course("MTH203", "Linear Algebra", 3, "A")
        );
        mathematicsStudent.registerCourse(
                new Course("MTH205", "Differential Equations", 3, "B")
        );
        mathematicsStudent.registerCourse(
                new Course("STA201", "Probability", 3, "B")
        );
        mathematicsStudent.registerCourse(
                new Course("CSC201", "Programming Fundamentals", 2, "C")
        );

        Student physicsStudent = new Student(
                "David James",
                "UI/PHY/2026/003",
                "Physics"
        );

        physicsStudent.registerCourse(
                new Course("PHY201", "Classical Mechanics", 3, "A")
        );
        physicsStudent.registerCourse(
                new Course("PHY203", "Electricity and Magnetism", 3, "B")
        );
        physicsStudent.registerCourse(
                new Course("PHY205", "Thermodynamics", 3, "A")
        );
        physicsStudent.registerCourse(
                new Course("MTH201", "Applied Mathematics", 3, "B")
        );
        physicsStudent.registerCourse(
                new Course("PHY207", "Practical Physics", 2, "A")
        );

        students.add(computerScienceStudent);
        students.add(mathematicsStudent);
        students.add(physicsStudent);

        refreshStudentRecords();
        studentRecordBox.setSelectedIndex(0);
        currentStudent = null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentCGPACalculatorGUI application =
                    new StudentCGPACalculatorGUI();

            application.setVisible(true);
        });
    }
}