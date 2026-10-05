// 2. DAO theo kiểu Lab 1 (composition) + logic đã sửa
package DataObject.DAO;
import Core.Interfaces.IStudentDAO;
import Core.Entities.Student;

import Utilities.FileIO.IFileIO;
import Utilities.FileIO.StudentFileIO;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class StudentDAO implements IStudentDAO {

    private static final String DEFAULT_PATH = "src/DataObject/data/Students.txt";

    private final IFileIO<Student> fileIO;
    private final List<Student> students;

    public StudentDAO() {
        this(new StudentFileIO(DEFAULT_PATH));
    }

    public StudentDAO(IFileIO<Student> fileIO) {
        this.fileIO = fileIO;
        this.students = loadFromFile();
    }

    private List<Student> loadFromFile() {
        try {
            return fileIO.readFromFile();
        } catch (Exception e) {
            System.out.println("Cannot read students: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Student> readAll() { return new ArrayList<>(students); }


    @Override
    public boolean writeAll(List<Student> list) {
        if (list == null) return false;
        students.clear();
        students.addAll(list);          // đồng bộ RAM trước khi ghi file
        return save();
    }

    @Override
    public boolean save() {
        try {
            return fileIO.saveToFile(students);
        } catch (Exception e) {
            System.out.println("Cannot save students: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean add(Student s) {
        return students.add(s);
    }

    @Override
    public boolean update(Student s) {
        if (s == null) return false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equalsIgnoreCase(s.getId())) {
                students.set(i, s);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        return id != null && students.removeIf(s -> s.getId().equalsIgnoreCase(id.trim()));
    }

    @Override
    public Optional<Student> findByID(String id) {
        return students.stream()
                .filter(s -> s.getId().equalsIgnoreCase(id.trim()))
                .findFirst();
    }


}