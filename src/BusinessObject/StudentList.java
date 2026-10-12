package BusinessObject;

import Core.Entities.Student;
import Core.Interfaces.ICourseDAO;
import Core.Interfaces.IStudentDAO;
import Utilities.Validation.StudentValidation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentList {

    private final IStudentDAO studentDAO;
    private final ICourseDAO courseDAO;   // dùng để chặn xóa sinh viên đang có khóa học
    private boolean dirty = false;

    public StudentList(IStudentDAO studentDAO, ICourseDAO courseDAO) {
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;

    }

    public boolean isDirty() { return dirty; }
    public List<Student> getAll() {
        return studentDAO.readAll();
    }

    public Optional<Student> findById(String id) {
        return (id == null) ? Optional.<Student>empty() : studentDAO.findByID(id);
    }

    public boolean add(Student s) {
        validate(s);
        if (findById(s.getId()).isPresent()) {
            throw new IllegalArgumentException("Student ID already exists: " + s.getId());
        }
        studentDAO.add(s);
        return studentDAO.save();
    }

    public boolean update(Student s) {
        validate(s);
        if (!findById(s.getId()).isPresent()) {
            throw new IllegalArgumentException("Student not found: " + s.getId());
        }
        studentDAO.update(s);
        return studentDAO.save();
    }

    public boolean delete(String id) {
        if (!findById(id).isPresent()) {
            throw new IllegalArgumentException("Student not found: " + id);
        }
        boolean enrolled = courseDAO.readAll().stream()
                .anyMatch(c -> c.getStudent().getId().equalsIgnoreCase(id.trim()));
        if (enrolled) {
            throw new IllegalArgumentException("Cannot delete: student is enrolled in course(s).");
        }
        studentDAO.delete(id);
        return studentDAO.save();
    }

    public List<Student> findByMajor(String major) {
        String k = major.trim().toLowerCase();
        return studentDAO.readAll().stream()
                .filter(s -> s.getMajor().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }

    public List<Student> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return new ArrayList<>();
        String k = keyword.trim().toLowerCase();
        return studentDAO.readAll().stream()
                .filter(s -> s.getName().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }

    public List<Student> sortByGpaAsc() {
        List<Student> list = studentDAO.readAll();
        list.sort(Comparator.comparingDouble(Student::getGpa));
        return list;
    }

    // Chuẩn hóa + kiểm tra luật
    private void validate(Student s) {
        if (s == null) throw new IllegalArgumentException("Student is null.");
        if (!StudentValidation.isValidId(s.getId()))
            throw new IllegalArgumentException("Invalid ID (format STU0000).");
        if (!StudentValidation.isValidName(s.getName()))
            throw new IllegalArgumentException("Invalid name (letters and spaces only).");
        if (!StudentValidation.isValidMajor(s.getMajor()))
            throw new IllegalArgumentException("Major must not be empty.");
        if (!StudentValidation.isValidGpa(s.getGpa()))
            throw new IllegalArgumentException("GPA must be from 0.0 to 4.0.");
        s.setId(s.getId().trim().toUpperCase());
        s.setName(s.getName().trim());
        s.setMajor(s.getMajor().trim());
    }

    public boolean save() {
        boolean ok = studentDAO.save();
        if (ok) dirty = false;
        return ok;
    }
}