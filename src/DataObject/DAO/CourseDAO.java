package DataObject.DAO;

import Core.Interfaces.ICourseDAO;
import Core.Entities.Course;

import Utilities.FileIO.IFileIO;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class CourseDAO implements ICourseDAO{
    private static final String DEFAULT_PATH = "src/DataObject/data/Course.txt";
    private final IFileIO<Course> fileIO;
    private final List<Course> courses;


    public CourseDAO(IFileIO<Course> fileIO) {
        this.fileIO = fileIO;
        this.courses = loadFromFile();
    }

    public List<Course> loadFromFile(){
        try{
            return fileIO.readFromFile();
        }catch (Exception e){
            System.out.println("Can't read course: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Course> readAll() { return new ArrayList<>(courses); }

    @Override
    public boolean writeAll(List<Course> o){
        courses.clear();
        courses.addAll(o);
        return save();
    }

    @Override
    public boolean save(){
        try{
            return fileIO.saveToFile(courses);
        }catch (Exception e){
            System.out.println("Can't not save: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean add(Course s) {
        return courses.add(s);
    }

    @Override
    public boolean update(Course s) {
        if (s == null) return false;
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseId().equalsIgnoreCase(s.getCourseId())) {
                courses.set(i, s);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        return id != null && courses.removeIf(s -> s.getCourseId().equalsIgnoreCase(id.trim()));
    }

    @Override
    public Optional<Course> findByID(String id) {
        return courses.stream()
                .filter(s -> s.getCourseId().equalsIgnoreCase(id.trim()))
                .findFirst();
    }

    @Override
    public List<Course> findByName(String name) {
        List<Course> result = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) {
            return result;
        }
        String keyword = name.trim().toLowerCase();

        for (Course c : courses) {
            if (c.getCourseName() != null && c.getCourseName().toLowerCase().contains(keyword)) {
                result.add(c);
            }
        }
        return result;
    }
}
