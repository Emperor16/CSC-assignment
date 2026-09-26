import java.util.ArrayList;

public class Student extends Person {

    private String studentId;
    private String department;
    private ArrayList<Course> courses;

    public Student(String name, String studentId, String department) {
        super(name);

        this.studentId = studentId;
        this.department = department;
        this.courses = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }


    // Method to add course
    public void registerCourse(Course course) {
        courses.add(course);
    }

    public double calculateCGPA() {

        if (courses.size() < 5) {
            return -1;
        }

        int totalPoints = 0;
        int totalCreditUnits = 0;

        for (Course course : courses) {

            totalPoints += course.getGradePoint()
                    * course.getCreditUnit();

            totalCreditUnits += course.getCreditUnit();
        }

        if (totalCreditUnits == 0) {
            return 0;
        }

        return (double) totalPoints / totalCreditUnits;
    }
}