package Utilities.FileIO;

import Core.Entities.Course;
import Core.Entities.Student;
import Utilities.FileIO.FileHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CourseFileIO extends FileHelper<Course> {

    private final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private final Map<String, Student> students; // key = studentId

    public CourseFileIO(String path, Map<String, Student> students) {
        super(path);
        this.students = students;
        this.df.setLenient(false); // 31/02/2025 -> lỗi
    }

    // courseId, studentId, courseName, duration, startDate
    @Override
    protected Course parse(String line) throws Exception {
        String[] t = line.split(",");
        if (t.length != 5) {
            throw new Exception("expected 5 fields, got " + t.length);
        }

        String courseId   = t[0].trim();
        String studentId  = t[1].trim();
        String courseName = t[2].trim();
        int duration      = Integer.parseInt(t[3].trim());
        Date startDate    = df.parse(t[4].trim());

        if (courseId.isEmpty() || courseName.isEmpty()) {
            throw new Exception("empty courseId/courseName");
        }
        if (duration < 5 || duration > 10) {
            throw new Exception("duration must be 5..10");
        }

        Student s = students.get(studentId);
        if (s == null) {
            throw new Exception("student not found: " + studentId);
        }

        return new Course(courseId, s, courseName, duration, startDate);
    }

    @Override
    protected String format(Course c) {
        return c.toDataString();
    }
}