package Core.Entities;

import java.util.Date;
import java.util.Objects;

// Quan hệ nhiều-nhiều giữa Student và Course
public class Registration {

    private Student studentId;
    private Course courseId;
    private Date registerDate;

    public Registration() {
    }

    public Registration(Student studentId, Course courseId, Date registerDate) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.registerDate = registerDate;
    }

    public Student getStudentId() { return studentId; }
    public void setStudentId(Student studentId) { this.studentId = studentId; }

    public Course getCourseId() { return courseId; }
    public void setCourseId(Course courseId) { this.courseId = courseId; }

    public Date getRegisterDate() { return registerDate; }
    public void setRegisterDate(Date registerDate) { this.registerDate = registerDate; }

    // Một sinh viên không đăng ký trùng một khóa học
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Registration)) return false;
        Registration r = (Registration) o;
        return Objects.equals(studentId, r.studentId) && Objects.equals(courseId, r.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseId);
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-8s | %tF", studentId, courseId, registerDate);
    }
}